package controller;

import external.AuthenticationService;
import external.EmailService;
import model.*;
import view.View;

/**
 * Controller responsible for displaying and handling user interaction with the main menu,
 * based on the role of the currently logged-in user (Guest, Student, Teaching Staff, Admin Staff).
 */
public class MenuController extends Controller {
    /**
     * Constructs a MenuController with shared context, view, authentication, and email services.
     *
     * @param sharedContext the context shared between controllers
     * @param view the view interface for displaying messages and menus
     * @param auth the authentication service
     * @param email the email service
     */
    public MenuController(SharedContext sharedContext, View view, AuthenticationService auth, EmailService email) {
        super(sharedContext, view, auth, email);
    }

    /**
     * Enum representing main menu options for Guest users.
     */
    public enum GuestMainMenuOption {
        LOGIN,
        CONSULT_FAQ,
        CONTACT_STAFF,
        VIEW_COURSES,
        VIEW_SPECIFIC_COURSE,
    }
    /**
     * Enum representing main menu options for Student users.
     */
    public enum StudentMainMenuOption {
        LOGOUT,
        CONSULT_FAQ,
        CONTACT_STAFF,
        VIEW_COURSES,
        VIEW_SPECIFIC_COURSE,
        MANAGE_TIMETABLE,
    }
    /**
     * Enum representing main menu options for Teaching Staff users.
     */
    public enum TeachingStaffMainMenuOption {
        LOGOUT,
        MANAGE_RECEIVED_QUERIES,
    }
    /**
     * Enum representing main menu options for Admin Staff users.
     */
    public enum AdminStaffMainMenuOption {
        LOGOUT,
        MANAGE_QUERIES,
        MANAGE_FAQ,
        MANAGE_COURSES
    }
    /**
     * Displays the main menu and handles menu option selection based on user role.
     * Loops until the user chooses to exit.
     */
    public void mainMenu() {
        boolean endLoop = false;
        while (!endLoop) {
            String userRole;
            if (sharedContext.currentUser instanceof Guest) {
                userRole = "Guest";
            } else if (sharedContext.currentUser instanceof AuthenticatedUser) {
                userRole = ((AuthenticatedUser) sharedContext.currentUser).getRole();
            } else {
                view.displayError("Main menu not implemented for current user type");
                return;
            }

            view.displayInfo("Hello! What would you like to do?");

            switch (userRole) {
                case "Guest" -> endLoop = handleGuestMainMenu();
                case "Student" -> endLoop = handleStudentMainMenu();
                case "TeachingStaff" -> endLoop = handleTeachingStaffMainMenu();
                case "AdminStaff" -> endLoop = handleAdminStaffMainMenu();
            }
        }

        view.displayInfo("Bye bye!");
    }

    /**
     * Handles the main menu for Guest users.
     *
     * @return true if the user chooses to exit, false otherwise
     */
    private boolean handleGuestMainMenu() {
        int optionNo = selectFromMenu(GuestMainMenuOption.values(), "Exit");
        if (optionNo == -1) {
            return true;
        }
        GuestMainMenuOption option = GuestMainMenuOption.values()[optionNo];
        switch (option) {
            case LOGIN -> new GuestController(sharedContext, view, auth, email).login();
            case CONSULT_FAQ -> new InquirerController(sharedContext, view, auth, email).consultFAQ();
            case CONTACT_STAFF -> new InquirerController(sharedContext, view, auth, email).contactStaff();
            case VIEW_COURSES -> new ViewerController(sharedContext, view, auth, email).viewCourses();
            case VIEW_SPECIFIC_COURSE -> new ViewerController(sharedContext, view, auth, email).viewCourse();

        }
        return false;
    }

    /**
     * Handles the main menu for Student users.
     *
     * @return true if the user chooses to exit, false otherwise
     */
    private boolean handleStudentMainMenu() {
        int optionNo = selectFromMenu(StudentMainMenuOption.values(), "Exit");
        if (optionNo == -1) {
            return true;
        }
        StudentMainMenuOption option = StudentMainMenuOption.values()[optionNo];
        switch (option) {
            case LOGOUT -> new AuthenticatedUserController(sharedContext, view, auth, email).logout();
            case CONSULT_FAQ -> new InquirerController(sharedContext, view, auth, email).consultFAQ();
            case CONTACT_STAFF -> new InquirerController(sharedContext, view, auth, email).contactStaff();
            case MANAGE_TIMETABLE -> new StudentController(sharedContext, view, auth, email).manageTimetable();
            case VIEW_COURSES -> new ViewerController(sharedContext, view, auth, email).viewCourses();
            case VIEW_SPECIFIC_COURSE -> new ViewerController(sharedContext, view, auth, email).viewCourse();
        }
        return false;
    }
    /**
     * Handles the main menu for Teaching Staff users.
     *
     * @return true if the user chooses to exit, false otherwise
     */
    private boolean handleTeachingStaffMainMenu() {
        int optionNo = selectFromMenu(TeachingStaffMainMenuOption.values(), "Exit");
        if (optionNo == -1) {
            return true;
        }
        TeachingStaffMainMenuOption option = TeachingStaffMainMenuOption.values()[optionNo];
        switch (option) {
            case LOGOUT -> new AuthenticatedUserController(sharedContext, view, auth, email).logout();
            case MANAGE_RECEIVED_QUERIES -> new TeachingStaffController(sharedContext, view, auth, email).manageReceivedInquiries();
        }
        return false;
    }

    /**
     * Handles the main menu for Admin Staff users.
     *
     * @return true if the user chooses to exit, false otherwise
     */
    private boolean handleAdminStaffMainMenu() {
        int optionNo = selectFromMenu(AdminStaffMainMenuOption.values(), "Exit");
        if (optionNo == -1) {
            return true;
        }
        AdminStaffMainMenuOption option = AdminStaffMainMenuOption.values()[optionNo];
        switch (option) {
            case LOGOUT -> new AuthenticatedUserController(sharedContext, view, auth, email).logout();
            case MANAGE_FAQ -> new AdminStaffController(sharedContext, view, auth, email).manageFAQ();
            case MANAGE_QUERIES -> new AdminStaffController(sharedContext, view, auth, email).manageInquiries();
            case MANAGE_COURSES -> new AdminStaffController(sharedContext, view, auth, email).manageCourses();
        }
        return false;
    }
}

