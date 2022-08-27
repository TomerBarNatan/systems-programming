package bgu.spl.net.PassiveObjects;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class gets a courses text file and extracts the courses by number, name,
 * previous courses and maximal participants. The input reader corrects the order of the
 * previous courses by their order in the input file.
 */
public class InputReader {
    private ArrayList<Short> ordered_courses;

    /**
     *extracts the courses from file.
     * @param file_path of courses
     * @return concurrent hash map of courses by their number
     * @throws IOException if file wasn't found
     */
    public ConcurrentHashMap<Short, Course> get_courses_from_file(String file_path) throws IOException {
        ordered_courses = new ArrayList<>();
        ConcurrentHashMap<Short, Course> courses_by_number = new ConcurrentHashMap<>();

        File file = new File(file_path);
        Course current_course;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            current_course = string_to_course(line.split("\\|"));
            courses_by_number.put(current_course.get_course_number(), current_course);
            ordered_courses.add(current_course.get_course_number());
        }

        sort_previous_courses(ordered_courses, courses_by_number);
        return courses_by_number;


    }

    public ArrayList<Short> get_ordered_courses() {
        return ordered_courses;
    }

    private static Course string_to_course(String[] arguments) {

        short course_number = Short.parseShort(arguments[0]);
        String course_name = arguments[1];
        String[] previous_courses_strings =
                arguments[2].replace("[", "").replace("]", "").split(",");
        ArrayList<Short> previous_courses_numbers = new ArrayList<>();
        if (!previous_courses_strings[0].equals("")) {
            for (int i = 0; i < previous_courses_strings.length; i++) {
                previous_courses_numbers.add(Short.parseShort(previous_courses_strings[i]));
            }
        }
        int maximal_number_of_students = Integer.parseInt(arguments[3]);
        return new Course(course_number, course_name, previous_courses_numbers, maximal_number_of_students);
    }

    private static void sort_previous_courses(ArrayList<Short> ordered_courses, ConcurrentHashMap<Short, Course> courses){

        for(Course course: courses.values()){
            ArrayList<Short> sorted_courses = new ArrayList<>();
            for(short ordered_course: ordered_courses){
                if(course.get_previous_courses().contains(ordered_course)){
                    sorted_courses.add(ordered_course);
                }
            }
            course.set_previous_courses(sorted_courses);
        }
    }
}
