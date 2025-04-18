package system_tests;

import controller.AdminStaffController;
import controller.MenuController;
import controller.ViewerController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;


public class AddCourseSystemTest extends TUITest {

    @Test
    public void testFullAddCourse() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "1", "Y",
                "1", "2025-04-10", "2025-04-11", "09:00", "10:00",
                "Room 101", "MONDAY", "lecture", "true",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("SUCCESS");
    }

    @Test
    public void testFullAddLab() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "1", "Y",
                "1", "2025-04-10", "2025-04-11", "09:00", "10:00",
                "Room 101", "MONDAY", "lab", "30",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("SUCCESS");
    }

    @Test
    public void testFullAddTutorial() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "1", "Y",
                "1", "2025-04-10", "2025-04-11", "09:00", "10:00",
                "Room 101", "MONDAY", "tutorial", "30",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("SUCCESS");
    }

    @Test
    public void testActivityErrors() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "1", "Y",
                "1",
                "asdasdf", "2025-04-10", "2025-03-11", "2025-04-11",
                "asdasdfasdf", "09:00", "08:00", "10:00",
                "", "Room 101",
                "asdsadf","MONDAY",
                "asasdfsadf", "lecture",
                "sdfwefef", "true",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("SUCCESS");
    }

    @Test
    public void testActivityLab() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "1", "Y",
                "1",
                "asdasdf", "2025-04-10", "2025-03-11", "2025-04-11",
                "asdasdfasdf", "09:00", "08:00", "10:00",
                "", "Room 101",
                "asdsadf","MONDAY",
                "asasdfsadf", "lab",
                "sdfwefef", "3",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("SUCCESS");
    }

    @Test
    public void testInvalidIntegerActivity() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "dddd", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }

    @Test
    public void testInvalidEmailOrganiser() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "asdfasdf", "Joash Lemmings",
                "jasdfasdf", "2", "2", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }

    @Test
    public void testNullInfo1() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "3", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }

    @Test
    public void testNullInfo2() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "3", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }

    @Test
    public void testNullInfo3() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "3", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }

    @Test
    public void testNullInfo4() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "3", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }

    @Test
    public void testNullInfo5() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "",
                "joash_smith@gmail.com", "2", "3", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }

    @Test
    public void testNullInfo6() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "", "2", "3", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }

    @Test
    public void testNullInfo7() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "", "3", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }

    @Test
    public void testNullInfo8() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "3", "", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }


    @Test
    public void testFillCourseInfoNotRecorded() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "1", "n",
                "1", "2025-04-10", "2025-04-11", "09:00", "10:00",
                "Room 101", "MONDAY", "lecture", "true",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("SUCCESS");
    }

    @Test
    public void testInvalidCourseInfoCode() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "dfdsfsf", "Math For Computer Science", "fun course", "Sarah Smith",
                "sarah_smith@gmail.com", "Joash Lemmings", "joash_smith@gmail.com",
                "2", "1", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Provided courseCode is invalid");
    }


    @Test
    public void testInvalidCourseInfoTutorial() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345", "Math For Computer Science", "fun course", "Sarah Smith",
                "sarah_smith@gmail.com", "Joash Lemmings", "joash_smith@gmail.com",
                "3", "adsasdfa", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Required course info not provided");
    }

    @Test
    public void testCheckCourseInfo() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        Boolean checkCourseCodeRegexMatch = context.getCourseManager().checkCourseCode("MATH12345");
        assertEquals(true, checkCourseCodeRegexMatch);

        Boolean checkCourseCodeInvalid = context.getCourseManager().checkCourseCode("Math For Computer Science");
        assertEquals(false, checkCourseCodeInvalid);

        Boolean checkCourseCodeInvalid2 = context.getCourseManager().checkCourseCode("MATHE12345");
        assertEquals(false, checkCourseCodeInvalid2);

        Boolean checkCourseCodeInvalid3 = context.getCourseManager().checkCourseCode("MAE12345");
        assertEquals(false, checkCourseCodeInvalid3);
    }

    public void populateCourseList(CourseManager courseManager) {
        Course course1 = new Course(
                "COMS10101",
                "Introduction to Computer Science",
                "Learn the fundamentals of computer science and programming.",
                false,
                "Alice Johnson",
                "alice.johnson@example.com",
                "Bob Smith",
                "bob.smith@example.com",
                2,
                1
        );

        course1.addActivity(
                courseManager.generateActivityId(),
                LocalDate.of(2025, 4, 10),
                LocalTime.of(9, 0),
                LocalDate.of(2025, 4, 10),
                LocalTime.of(10, 0),
                "Main Lecture Hall",
                DayOfWeek.MONDAY,
                true,
                "lecture"
        );

        course1.addActivity(
                courseManager.generateActivityId(),
                LocalDate.of(2025, 4, 11),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 4, 11),
                LocalTime.of(11, 0),
                "LAB Room 1",
                DayOfWeek.TUESDAY,
                30,
                "lab"
        );

        course1.addActivity(
                courseManager.generateActivityId(),
                LocalDate.of(2025, 4, 12),
                LocalTime.of(11, 0),
                LocalDate.of(2025, 4, 12),
                LocalTime.of(12, 0),
                "Tutorial Room",
                DayOfWeek.WEDNESDAY,
                20,           // capacity
                "tutorial"    // type indicator
        );

        Course course2 = new Course(
                "MATH20120",
                "Calculus II",
                "Advanced calculus topics including integration techniques.",
                false,
                "Carol Davis",
                "carol.davis@example.com",
                "David Miller",
                "david.miller@example.com",
                0,
                0
        );

        course2.addActivity(
                courseManager.generateActivityId(),
                LocalDate.of(2025, 4, 13),
                LocalTime.of(8, 0),
                LocalDate.of(2025, 4, 13),
                LocalTime.of(9, 0),
                "Lecture Hall 2",
                DayOfWeek.THURSDAY,
                true,         // lecture
                "lecture"
        );
        course2.addActivity(
                courseManager.generateActivityId(),
                LocalDate.of(2025, 4, 14),
                LocalTime.of(9, 0),
                LocalDate.of(2025, 4, 14),
                LocalTime.of(10, 0),
                "Tutorial Room 2",
                DayOfWeek.FRIDAY,
                15,           // capacity for tutorial
                "tutorial"
        );

        Course course3 = new Course(
                "PHYS10101",
                "General Physics",
                "Introduction to mechanics, thermodynamics, and electromagnetism.",
                true,
                "Eve Thompson",
                "eve.thompson@example.com",
                "Frank Lee",
                "frank.lee@example.com",
                3,
                2
        );

        course3.addActivity(
                courseManager.generateActivityId(),
                LocalDate.of(2025, 4, 15),
                LocalTime.of(13, 0),
                LocalDate.of(2025, 4, 15),
                LocalTime.of(14, 0),
                "Physics Lecture Hall",
                DayOfWeek.MONDAY,
                true,         // lecture
                "lecture"
        );
        course3.addActivity(
                courseManager.generateActivityId(),
                LocalDate.of(2025, 4, 16),
                LocalTime.of(14, 0),
                LocalDate.of(2025, 4, 16),
                LocalTime.of(15, 0),
                "Physics LAB",
                DayOfWeek.TUESDAY,
                25,           // capacity for lab
                "lab"
        );

        courseManager.addCourseToCourseList(course1);
        courseManager.addCourseToCourseList(course2);
        courseManager.addCourseToCourseList(course3);
    }

    @ Test
    public void testHasCourse() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH20120", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "1", "Y",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
        assertOutputContains("Course with that code already exists");

    }

    @ Test
    public void testAddCourseSuccess() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "1",
                "2025-04-10",
                "2025-04-11",
                "09:00",
                "10:00",
                "Room 101",
                "MONDAY",
                "lecture",
                "true"
        );

        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);

        CourseInfo newCourseInfoBasic = new CourseInfo();
        newCourseInfoBasic.setField("courseCode", "MATH20202");
        newCourseInfoBasic.setField("name", "Advanced Mathematics");
        newCourseInfoBasic.setField("description", "An in-depth course on advanced mathematics topics.");
        newCourseInfoBasic.setField("courseOrganiserName", "Carol Davis");
        newCourseInfoBasic.setField("courseOrganiserEmail", "carol@example.com");
        newCourseInfoBasic.setField("courseSecretaryName", "Dan Brown");
        newCourseInfoBasic.setField("courseSecretaryEmail", "dan@example.com");
        newCourseInfoBasic.setField("requiredTutorials", "3");
        newCourseInfoBasic.setField("requiredLabs", "2");
        // Update the boolean field for requiresComputers.
        newCourseInfoBasic.setRequiresComputers(true);
        AdminStaffController adminStaffController = new AdminStaffController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        CourseManager courseManager = context.getCourseManager();
        courseManager.addCourse("testing@gmail.com", newCourseInfoBasic);
        assertOutputContains("SUCCESS");


    }

    @ Test
    public void testAddCourseFailure() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "1",
                "2025-04-10",
                "2025-04-11",
                "09:00",
                "10:00",
                "Room 101",
                "MONDAY",
                "lecture",
                "true"
        );

        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);

        CourseInfo newCourseInfoBasic = new CourseInfo();
        newCourseInfoBasic.setField("courseCode", "MATH");
        newCourseInfoBasic.setField("name", "Advanced Mathematics");
        newCourseInfoBasic.setField("description", "An in-depth course on advanced mathematics topics.");
        newCourseInfoBasic.setField("courseOrganiserName", "Carol Davis");
        newCourseInfoBasic.setField("courseOrganiserEmail", "carol@example.com");
        newCourseInfoBasic.setField("courseSecretaryName", "Dan Brown");
        newCourseInfoBasic.setField("courseSecretaryEmail", "dan@example.com");
        newCourseInfoBasic.setField("requiredTutorials", "3");
        newCourseInfoBasic.setField("requiredLabs", "2");
        // Update the boolean field for requiresComputers.
        newCourseInfoBasic.setRequiresComputers(true);
        AdminStaffController adminStaffController = new AdminStaffController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        CourseManager courseManager = context.getCourseManager();
        courseManager.addCourse("testing@gmail.com", newCourseInfoBasic);
        System.out.println(System.out);
        assertOutputContains("Provided courseCode is invalid");


    }
}