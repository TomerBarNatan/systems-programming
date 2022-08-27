package bgu.spl.net.PassiveObjects;

import bgu.spl.net.Users.Student;

import java.util.ArrayList;


public class Course {
    private final short number_of_course;
    private final String name_of_course;
    private ArrayList<Short> previous_courses;
    private final int maximal_student_count;
    private int registered_students;
    private final ArrayList<String> student_names;

    public Course(short number_of_course, String name_of_course, ArrayList<Short> previous_courses, int maximal_student_count) {
        this.number_of_course = number_of_course;
        this.name_of_course = name_of_course;
        this.previous_courses = previous_courses;
        this.maximal_student_count = maximal_student_count;
        this.registered_students = 0;
        this.student_names = new ArrayList<>();
    }

    public short get_course_number(){
        return number_of_course;
    }

    public void set_previous_courses( ArrayList<Short> courses){
        previous_courses = courses;
    }

    public String get_name_of_course() {
        return name_of_course;
    }

    public int get_maximal_student_count() {
        return maximal_student_count;
    }

    public int get_registered_students() {
        return registered_students;
    }

    public synchronized boolean register(Student student){
        if(registered_students < maximal_student_count && student.check_previous_courses(previous_courses)){
            student_names.add(student.get_username());
            registered_students++;
            return true;
        }
        return false;
    }

    public ArrayList<Short> get_previous_courses() {
        return previous_courses;
    }

    public ArrayList<String> get_student_names() {
        return student_names;
    }

    public synchronized void unregister(String username) {
        student_names.remove(username);
        registered_students--;
    }
}

