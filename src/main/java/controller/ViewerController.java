package controller;

import external.AuthenticationService;
import external.EmailService;
import model.Course;
import model.CourseManager;
import model.SharedContext;
import view.View;

public class ViewerController extends Controller {

    public ViewerController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    public void viewCourses() {
        CourseManager courseManager = sharedContext.getCourseManager();
        courseManager.viewCourses();
    }

    public void viewCourse(){
        CourseManager courseManager = sharedContext.getCourseManager();
        String courseCode = view.getInput("Enter the course code: ");
        courseManager.viewCourse(courseCode);
    }
}
