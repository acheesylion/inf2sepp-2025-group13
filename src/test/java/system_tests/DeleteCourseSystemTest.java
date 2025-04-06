package system_tests;

import controller.AdminStaffController;
import controller.AuthenticatedUserController;
import controller.InquirerController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.FAQ;
import model.*;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteCourseSystemTest extends TUITest {
    @Test
    public void DeleteCourse() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MockEmailService mockEmail = new MockEmailService();
        CourseManager courseManager = context.getCourseManager(); // ensure it's the same manager context uses
        Course course = new Course(
                "COMP12345",                       // courseCode (valid)
                "Introduction to Programming",     // name
                "Learn basic programming concepts",// description
                true,                              // requiresComputers
                "Dr. Alice Smith",                 // courseOrganiserName
                "alice.smith@university.edu",      // courseOrganiserEmail
                "Bob Johnson",                     // courseSecretaryName
                "bob.johnson@university.edu",      // courseSecretaryEmail
                0,                                // requiredTutorials
                0                                  // requiredLabs
        );
        courseManager.addCourseToCourseList(course);

        assertTrue(courseManager.hasCourse("COMP12345"), "Course should exist before deletion");
        loginAsAdminStaff(context);
        AdminStaffController admin = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        courseManager.removeCourse("COMP12345");
        assertFalse(courseManager.hasCourse("COMP12345"), "Course shouldnt exist after deletion");



    }

    @Test
    public void Checkdeletecourseemails() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MockEmailService mockEmail = new MockEmailService();
        CourseManager courseManager = context.getCourseManager(); // ensure it's the same manager context uses
        Course course = new Course(
                "COMP12345",                       // courseCode (valid)
                "Introduction to Programming",     // name
                "Learn basic programming concepts",// description
                true,                              // requiresComputers
                "Dr. Alice Smith",                 // courseOrganiserName
                "alice.smith@university.edu",      // courseOrganiserEmail
                "Bob Johnson",                     // courseSecretaryName
                "bob.johnson@university.edu",      // courseSecretaryEmail
                0,                                // requiredTutorials
                0                                  // requiredLabs
        );
        courseManager.addCourseToCourseList(course);
        course.addMember("Student1@email.com");
        course.addMember("Student2@email.com");

        loginAsAdminStaff(context);
        AdminStaffController admin = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();

        admin.deletecourselogic("COMP12345");
        assertOutputContains("Email from " + context.getCurrentUserEmail()+  " to Student1@email.com");
        assertOutputContains("Email from " + context.getCurrentUserEmail()+  " to Student2@email.com");
        assertOutputContains("Email from " + context.getCurrentUserEmail()+  " to alice.smith@university.edu");


    }
}
