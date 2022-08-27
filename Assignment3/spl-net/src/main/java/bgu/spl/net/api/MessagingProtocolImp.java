package bgu.spl.net.api;

import bgu.spl.net.PassiveObjects.Course;
import bgu.spl.net.PassiveObjects.Database;
import bgu.spl.net.PassiveObjects.Messages.*;
import bgu.spl.net.PassiveObjects.Messages.Error;
import bgu.spl.net.Users.Admin;
import bgu.spl.net.Users.Student;
import bgu.spl.net.Users.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MessagingProtocolImp implements MessagingProtocol<Message> {
    private Student student;
    private Admin admin;
    private final Database database;
    private boolean should_terminate;

    public MessagingProtocolImp() {
        this.database = Database.getInstance();
        this.should_terminate = false;
        this.student = null;
        this.admin = null;
    }

    public enum OPCODE {
        ADMINREG(1),
        STUDENTREG(2),
        LOGIN(3),
        LOGOUT(4),
        COURSEREG(5),
        KDAMCHECK(6),
        COURSESTAT(7),
        STUDENTSTAT(8),
        ISREGISTERED(9),
        UNREGISTER(10),
        MYCOURSES(11);

        private final int value;

        OPCODE(final int newValue) {
            value = newValue;
        }
    }

    @Override
    public Message process(Message message) {
        short opcode = message.getOpcode();
        OPCODE enumerated_opcode = OPCODE.values()[opcode-1];
        switch (enumerated_opcode) {
            case ADMINREG:
                return admin_register((UserInformation) message);
            case STUDENTREG:
                return student_register((UserInformation) message);
            case LOGIN:
                return login((UserInformation) message);
            case LOGOUT:
                return logout(opcode);
            case COURSEREG:
                return register_to_course((CourseInformation) message);
            case KDAMCHECK:
                return previous_courses((CourseInformation) message);
            case COURSESTAT:
                return course_status((CourseInformation) message);
            case STUDENTSTAT:
                return student_status((UsernameData) message);
            case ISREGISTERED:
                return is_registered((CourseInformation) message);
            case UNREGISTER:
                return unregister((CourseInformation) message);
            case MYCOURSES:
                return my_courses(opcode);
            default:
                return new Error(opcode);
        }
    }


    @Override
    public boolean shouldTerminate() {
        return should_terminate;
    }

    private ServerMessage admin_register(UserInformation user_information) {
        if(!client_connected()) {
            boolean registered = database.add_admin(user_information.getUserName(), user_information.getPassword());
            if (registered) {
                return new Acknowledge(user_information.getOpcode());
            }
        }
        return new Error(user_information.getOpcode());
    }

    private ServerMessage student_register(UserInformation user_information) {
        if(!client_connected()) {
            boolean registered = database.add_student(user_information.getUserName(), user_information.getPassword());
            if (registered) {
                return new Acknowledge(user_information.getOpcode());
            }
        }
        return new Error(user_information.getOpcode());
    }

    private ServerMessage login(UserInformation user_information) {
        User user = database.get_user(user_information.getUserName());
        if (user == null || !user.get_password().equals(user_information.getPassword())
                || user.is_logged_in() || client_connected()) {
            return new Error(user_information.getOpcode());
        }
        user.login();
        if (user instanceof Student) {
            student = (Student) user;
        } else {
            admin = (Admin) user;
        }

        return new Acknowledge(user_information.getOpcode());
    }

    private ServerMessage logout(short opcode) {
        if (student != null || admin != null) {
            if (student != null) {
                student.logout();
            }
            if (admin != null) {
                admin.logout();
            }
            should_terminate = true;
            return new Acknowledge(opcode);
        }
        return new Error(opcode);
    }

    private ServerMessage register_to_course(CourseInformation course_information) {
        if (student == null) {
            return new Error(course_information.getOpcode());
        }
        Course course = database.get_course(course_information.getCourseNumber());
        if (course == null || !course.register(student)) {
            return new Error(course_information.getOpcode());
        }

        student.register_to_course(course);
        return new Acknowledge(course_information.getOpcode());
    }

    private ServerMessage previous_courses(CourseInformation course_information) {
        Course course = database.get_course(course_information.getCourseNumber());
        if(student == null || course == null){
            return new Error(course_information.getOpcode());
        }
        Acknowledge ack = new Acknowledge(course_information.getOpcode());
        ack.setOptional(course.get_previous_courses().toString() + "\0");

        return ack;
    }

    private ServerMessage course_status(CourseInformation course_information){
        Course course = database.get_course(course_information.getCourseNumber());
        if(admin == null || course == null){
            return new Error(course_information.getOpcode());
        }
        Acknowledge ack = new Acknowledge(course_information.getOpcode());
        ack.setOptional(create_course_status_string(course));
        return ack;
    }

    private String create_course_status_string(Course course){
        String output = String.format("Course: (%d) %s\nSeats Available: %d/%d\nStudents Registered: ",
                course.get_course_number(),
                course.get_name_of_course(),
                course.get_maximal_student_count() - course.get_registered_students(),
                course.get_maximal_student_count());

        String students_string = Arrays.toString(course.get_student_names().stream().sorted(String::compareTo).toArray());
        students_string = students_string.replaceAll(" ","");;
        return output + students_string + "\0";
    }

    private ServerMessage student_status(UsernameData username_data){
        User user = database.get_user(username_data.getUsername());
        if(admin == null || user == null || user instanceof Admin){
            return new Error(username_data.getOpcode());
        }
        Acknowledge ack = new Acknowledge(username_data.getOpcode());
        ack.setOptional(create_student_status_string((Student)user));
        return ack;
    }

    private String create_student_status_string(Student student) {

        return "Student: " +
                student.get_username() +
                "\nCourses: " +
                courses_numbers_to_string(student.get_courses()).replaceAll("\\s+","") +
                "\0";
    }

    private ServerMessage is_registered(CourseInformation message) {
        Course course = database.get_course(message.getCourseNumber());
        if(student == null || course == null){
            return new Error(message.getOpcode());
        }
        Acknowledge ack = new Acknowledge(message.getOpcode());
        if(student.is_registered(course.get_course_number())) {
            ack.setOptional("REGISTERED\0");
        }
        else{
            ack.setOptional("NOT REGISTERED\0");
        }
        return ack;
    }

    private ServerMessage unregister(CourseInformation message) {
        short course_number = message.getCourseNumber();
        short opcode = message.getOpcode();
        Course course = database.get_course(course_number);
        if(student == null || course == null || !student.is_registered(course_number)){
            return new Error(opcode);
        }
        course.unregister(student.get_username());
        student.unregister(course_number);
        return new Acknowledge(opcode);
    }

    private ServerMessage my_courses(short opcode) {
        if(student == null){
            return new Error(opcode);
        }
        Acknowledge ack = new Acknowledge(opcode);
        ack.setOptional(courses_numbers_to_string(student.get_courses()).replaceAll("\\s+","")+"\0");
        return ack;
    }

    private boolean client_connected(){
        return student != null | admin != null;
    }

    private String courses_numbers_to_string(HashMap<Short, Course> courses_map){
        ArrayList<Short> sorted_courses = new ArrayList<>();
        for(short course_number: database.get_ordered_courses()){
            if(courses_map.containsKey(course_number)){
                sorted_courses.add(course_number);
            }
        }
        return Arrays.toString(sorted_courses.toArray());
    }
}
