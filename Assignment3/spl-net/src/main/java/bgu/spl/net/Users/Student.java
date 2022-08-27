package bgu.spl.net.Users;

import bgu.spl.net.PassiveObjects.Course;

import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User{
    private final HashMap<Short, Course> courses;

    public Student(String username, String password) {
        super(username, password);
        courses = new HashMap<>();
    }

    public void register_to_course(Course course){
        courses.put(course.get_course_number(), course);
    }

    public boolean is_registered(short course_number){
        return courses.containsKey(course_number);
    }

    public void unregister(short course_number){
        courses.remove(course_number);
    }

    public boolean check_previous_courses(ArrayList<Short> previous_courses){
        for(short course: previous_courses){
            if(!courses.containsKey(course))
                return false;
        }
        return true;
    }

    public HashMap<Short, Course> get_courses() {
        return courses;
    }
}
