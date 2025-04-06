package controller;

import external.AuthenticationService;
import external.EmailService;
import model.*;
import view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class responsible for managing student-related actions,
 * particularly those involving the student's timetable such as adding or removing courses
 * and selecting activities.
 */
public class StudentController extends Controller {

    /**
     * Constructs a new StudentController instance.
     *
     * @param sharedContext the shared application context
     * @param view the view used to interact with the user
     * @param auth the authentication service
     * @param email the email service
     */
    public StudentController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    /**
     * Enum representing the options available when managing the timetable.
     */
    private enum manageTimetableOptions {
        ADD_COURSE_TO_TIMETABLE,
        REMOVE_COURSE_FROM_TIMETABLE,
        VIEW_TIMETABLE,
        CHOOSE_ACTIVITY_FOR_COURSE,
    }

    /**
     * Begins the interactive session for managing the student's timetable.
     * The loop continues until the user chooses to exit.
     */
    public void manageTimetable() {
        boolean endLoop = false;
        while (!endLoop) {
            endLoop = handleMangeTimetable();
        }
    }

    /**
     * Handles the selection of a timetable management option from the menu.
     *
     * @return true if the user chooses to exit; false otherwise
     */
    private boolean handleMangeTimetable() {
        int optionNo = selectFromMenu(manageTimetableOptions.values(), "Back to main menu");
        if (optionNo == -1) {
            return true;
        }
        manageTimetableOptions option = manageTimetableOptions.values()[optionNo];
        switch (option) {
            case ADD_COURSE_TO_TIMETABLE -> addCourseToTimetable();
            case REMOVE_COURSE_FROM_TIMETABLE -> removeCourseFromTimetable();
            case VIEW_TIMETABLE -> viewTimetable();
            case CHOOSE_ACTIVITY_FOR_COURSE -> chooseActivityForCourse();
        }
        return false;
    }

    /**
     * Prompts the student to input a course code and adds it to their timetable.
     */
    private void addCourseToTimetable() {
        view.displayInfo("=== Add Course to Timetable ===");
        String courseCode = view.getInput("Enter the course code: ");
        String email = sharedContext.getCurrentUserEmail();
        CourseManager courseManager = sharedContext.getCourseManager();
        courseManager.addCourseToStudentTimetable(email, courseCode);
    }

    /**
     * Prompts the student to input a course code and removes it from their timetable.
     */
    private void removeCourseFromTimetable() {
        view.displayInfo("=== Remove Course from Timetable ===");
        String courseCode = view.getInput("Enter the course code: ");
        String email = sharedContext.getCurrentUserEmail();
        CourseManager courseManager = sharedContext.getCourseManager();
        courseManager.removeCourseFromTimetable(email, courseCode);
    }

    /**
     * Displays the student's current timetable using the view.
     */
    private void viewTimetable() {
        CourseManager courseManager = sharedContext.getCourseManager();
        String email = sharedContext.getCurrentUserEmail();
        courseManager.printTimetable(email, view);
    }

    /**
     * Allows the student to select an activity for a course by entering a course code and activity ID.
     */
    private void chooseActivityForCourse() {
        view.displayInfo("=== Choose Activity for Course ===");
        String courseCode = view.getInput("Enter the course code: ");
        String activityId = view.getInput("Enter the Activity ID: ");
        String email = sharedContext.getCurrentUserEmail();
        CourseManager courseManager = sharedContext.getCourseManager();
        courseManager.chooseActivityForCourse(email, courseCode, activityId);
    }


}
