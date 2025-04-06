package controller;

import external.AuthenticationService;
import external.EmailService;
import model.Course;
import model.CourseManager;
import model.SharedContext;
import view.View;

/**
 * Controller class responsible for viewing course-related data.
 * Intended for users with viewing permissions (e.g., guest users, observers).
 */
public class ViewerController extends Controller {

    /**
     * Constructs a {@code ViewerController} with the required dependencies.
     *
     * @param sharedContext the shared application context containing course and user data
     * @param view the interface used for displaying output and capturing user input
     * @param auth the authentication service for managing user sessions
     * @param email the email service used for sending notifications (unused here but passed for consistency)
     */
    public ViewerController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    /**
     * Displays a list of all available courses using the course manager.
     */
    public void viewCourses() {
        CourseManager courseManager = sharedContext.getCourseManager();
        courseManager.viewCourses();
    }

    /**
     * Prompts the user to enter a course code and then displays detailed information
     * for the specified course using the course manager.
     */
    public void viewCourse(){
        CourseManager courseManager = sharedContext.getCourseManager();
        String courseCode = view.getInput("Enter the course code: ");
        courseManager.viewCourse(courseCode);
    }
}
