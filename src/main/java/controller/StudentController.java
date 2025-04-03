package controller;

import external.AuthenticationService;
import external.EmailService;
import model.*;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class StudentController extends Controller {
    public StudentController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }
    private enum manageTimetableOptions {
        ADD_COURSE_TO_TIMETABLE,
        REMOVE_COURSE_FROM_TIMETABLE,
        VIEW_TIMETABLE,
        TEST_ADD_COURSE,
    }

    public void manageTimetable() {
        boolean endLoop = false;
        while (!endLoop) {
            endLoop = handleMangeTimetable();
        }
    }

    private boolean handleMangeTimetable() {
        int optionNo = selectFromMenu(manageTimetableOptions.values(), "Back to main menu");
        if (optionNo == -1) {
            return true;
        }
        manageTimetableOptions option = manageTimetableOptions.values()[optionNo];
        switch (option) {
            case ADD_COURSE_TO_TIMETABLE -> addCourseToTimetable();
            case REMOVE_COURSE_FROM_TIMETABLE -> removeCourseFromTimetable();
            case VIEW_TIMETABLE -> viewTimeTable();
            case TEST_ADD_COURSE -> testAddCourseStudent();
        }
        return false;
    }

    private void addCourseToTimetable() {
        view.displayInfo("=== Add Course to Timetable ===");
        String courseCode = view.getInput("Enter the course code: ");
        String email = sharedContext.getCurrentUserEmail();
        CourseManager courseManager = sharedContext.getCourseManager();
        courseManager.addCourseToStudentTimetable(email, courseCode);
    }


    private void testAddCourseStudent() {
        CourseManager courseManager = sharedContext.getCourseManager();
        courseManager.testAddCourse();
    }

    private void removeCourseFromTimetable() {}

    private void viewTimeTable() {}

    private void chooseActivityForCourse() {
        view.displayInfo("=== Choose Activity for Course ===");
        String courseCode = view.getInput("Enter the course code: ");
        int activityID = Integer.parseInt(view.getInput("Enter the Activity ID: "));
        String email = sharedContext.getCurrentUserEmail();
        CourseManager courseManager = sharedContext.getCourseManager();
        courseManager.chooseActivityForCourse(email, courseCode, activityID);
    }

}
