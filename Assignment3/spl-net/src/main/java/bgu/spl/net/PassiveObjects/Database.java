package bgu.spl.net.PassiveObjects;

import bgu.spl.net.Users.Admin;
import bgu.spl.net.Users.Student;
import bgu.spl.net.Users.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the bgu.spl.net.PassiveObjects.Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private ConcurrentHashMap<Short, Course> course_by_number;
	private final ConcurrentHashMap<String, Student> usernames_to_students;
	private final ConcurrentHashMap<String, Admin> usernames_to_admins;

	InputReader input_reader;


	//to prevent user from creating new bgu.spl.net.PassiveObjects.Database
	private Database() {
	    course_by_number = new ConcurrentHashMap<>();
	    usernames_to_students = new ConcurrentHashMap<>();
	    usernames_to_admins = new ConcurrentHashMap<>();
	    input_reader = new InputReader();
	}

	private static class DatabaseHolder{
		private static final Database instance = new Database();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
	    return DatabaseHolder.instance;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the bgu.spl.net.PassiveObjects.Database, returns true if successful.
	 */
	public boolean initialize(String coursesFilePath) {
		try{
			course_by_number = input_reader.get_courses_from_file(coursesFilePath);
			return true;
		}
		catch (IOException error){
			error.printStackTrace();
		}
		return false;
	}

	/**
	 * gets course object by it's number
	 * @param course_number number of course
	 * @return course
	 */
	public Course get_course(short course_number){
		return course_by_number.get(course_number);
	}

	/**
	 *
	 * @return ordered courses
	 */
	public ArrayList<Short> get_ordered_courses(){
		return input_reader.get_ordered_courses();
	}

	/**
	 * Synchronized function that adds student to the student hash map, if wasn't added before
	 * and username doesn't belong to other admin
	 * @param username of student
	 * @param password of student
	 * @return true if succeeded false otherwise.
	 */
	public synchronized boolean add_student(String username, String password){
		if(!usernames_to_students.containsKey(username) && !usernames_to_admins.containsKey(username)){
			usernames_to_students.put(username, new Student(username, password));
			return true;
		}
		return false;
	}

	/**
	 * Synchronized function that adds admin to the admin hash map, if wasn't added before
	 * and username doesn't belong to another student
	 * @param username of admin
	 * @param password of admin
	 * @return true if succeeded false otherwise.
	 */
	public synchronized boolean add_admin(String username, String password){
		if(!usernames_to_admins.containsKey(username) && !usernames_to_students.containsKey(username)){
			usernames_to_admins.put(username, new Admin(username, password));
			return true;
		}
		return false;
	}

	public User get_user(String username){
		if(usernames_to_students.containsKey(username)){
			return usernames_to_students.get(username);
		}
		else{
			return usernames_to_admins.get(username);
		}
	}


}
