package system_tests;

import controller.AdminStaffController;
import controller.MenuController;
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
                "-1"
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
    public void testFillCourseInfoRecorded() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "1", "Y"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        CourseInfo newCourseInfoBasic = new CourseInfo();
        startOutputCapture();

        adminStaffController.fillCourseInfo(newCourseInfoBasic);
        assertInstanceOf(CourseInfo.class, newCourseInfoBasic);
    }

    @Test
    public void testFillCourseInfoNotRecorded() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "MATH12345", "Math For Computer Science", "fun course",
                "Sarah Smith", "sarah_smith@gmail.com", "Joash Lemmings",
                "joash_smith@gmail.com", "2", "1", "n"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        CourseInfo newCourseInfoBasic = new CourseInfo();
        startOutputCapture();

        adminStaffController.fillCourseInfo(newCourseInfoBasic);
        assertInstanceOf(CourseInfo.class, newCourseInfoBasic);
    }

    @Test
    public void testFillCourseInfoError() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "MATH12345",
                "Math For Computer Science",
                "fun course",
                "Sarah Smith",
                "sarah_smith@gmail.com",
                "Joash Lemmings",
                "joash_smith@gmail.com",
                "2",
                "1",
                "2039wfwe"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        CourseInfo newCourseInfoError = new CourseInfo();
        startOutputCapture();
        adminStaffController.fillCourseInfo(newCourseInfoError);
        assertEquals(false, newCourseInfoError.getRequiresComputers());
    }

    @Test
    public void testValidCourseInfo() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "MATH12345",
                "Math For Computer Science",
                "fun course",
                "Sarah Smith",
                "sarah_smith@gmail.com",
                "Joash Lemmings",
                "joash_smith@gmail.com",
                "2",
                "1",
                "Y"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        CourseInfo newCourseInfoBasic = new CourseInfo();
        startOutputCapture();
        adminStaffController.fillCourseInfo(newCourseInfoBasic);
        Boolean validCourseInfo = context.getCourseManager().validCourseInfo(newCourseInfoBasic);
        assertEquals(true, validCourseInfo);
    }

    @Test
    public void testInvalidCourseInfoCode() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "dfdsfsf",
                "Math For Computer Science",
                "fun course",
                "Sarah Smith",
                "sarah_smith@gmail.com",
                "Joash Lemmings",
                "joash_smith@gmail.com",
                "2",
                "1",
                "Y"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        CourseInfo newCourseInfoBasic = new CourseInfo();
        startOutputCapture();
        adminStaffController.fillCourseInfo(newCourseInfoBasic);
        Boolean validCourseInfo = context.getCourseManager().validCourseInfo(newCourseInfoBasic);
        // Check for Course Info in sequence diagram happens after validCourseInfo; this is correct.
        assertEquals(true, validCourseInfo);
    }

    @Test
    public void testInvalidCourseInfoLab() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "MATH12345",
                "Math For Computer Science",
                "fun course",
                "Sarah Smith",
                "sarah_smith@gmail.com",
                "Joash Lemmings",
                "joash_smith@gmail.com",
                "2",
                "adsasdfa",
                "Y"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        CourseInfo newCourseInfoBasic = new CourseInfo();
        startOutputCapture();
        adminStaffController.fillCourseInfo(newCourseInfoBasic);
        Boolean validCourseInfo = context.getCourseManager().validCourseInfo(newCourseInfoBasic);
        assertEquals(false, validCourseInfo);
    }

    @Test
    public void testInvalidCourseInfoTutorial() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "MATH12345",
                "Math For Computer Science",
                "fun course",
                "Sarah Smith",
                "sarah_smith@gmail.com",
                "Joash Lemmings",
                "joash_smith@gmail.com",
                "3",
                "adsasdfa",
                "Y"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        AdminStaffController adminStaffController = new AdminStaffController(context, view, new MockAuthenticationService(), new MockEmailService());
        CourseInfo newCourseInfoBasic = new CourseInfo();
        startOutputCapture();
        adminStaffController.fillCourseInfo(newCourseInfoBasic);
        Boolean validCourseInfo = context.getCourseManager().validCourseInfo(newCourseInfoBasic);
        assertEquals(false, validCourseInfo);
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
                LocalDate.of(2025, 4, 11),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 4, 11),
                LocalTime.of(11, 0),
                "Lab Room 1",
                DayOfWeek.TUESDAY,
                30,
                "lab"
        );

        course1.addActivity(
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
                LocalDate.of(2025, 4, 16),
                LocalTime.of(14, 0),
                LocalDate.of(2025, 4, 16),
                LocalTime.of(15, 0),
                "Physics Lab",
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
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        boolean hasCourseBool1 = courseManager.hasCourse("MATH20120");
        assertTrue(hasCourseBool1);
        boolean hasCourseBool2 = courseManager.hasCourse("dfdsa");
        assertFalse(hasCourseBool2);
    }

    @ Test
    public void testAddActivity() throws URISyntaxException, IOException, ParseException {
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
        loginAsAdminStaff(context);
        CourseManager courseManager = context.getCourseManager();
        startOutputCapture();
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
        courseManager.addActivitiesToCourse(course1, view);
        assertEquals(1, course1.getActivities().size(), "One activity should have been added to the course.");
        assertTrue(course1.getActivities().get(0) instanceof Lecture, "The added activity should be a Lecture.");
        assertEquals(DayOfWeek.MONDAY, course1.getActivities().get(0).getDay(), "The day should be MONDAY.");
        assertEquals("Room 101", course1.getActivities().get(0).getLocation(), "Location should be 'Room 101'.");
        assertTrue(((Lecture) course1.getActivities().get(0)).getRecorded(), "Lecture should be marked as recorded.");
    }

    @ Test
    public void testAddActivityMultiple() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "2",
                "2025-04-10",
                "2025-04-11",
                "09:00",
                "10:00",
                "Room 101",
                "MONDAY",
                "lecture",
                "true",
                "2025-04-10",
                "2025-04-11",
                "09:00",
                "10:00",
                "Room 102",
                "MONDAY",
                "lecture",
                "true"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        CourseManager courseManager = context.getCourseManager();
        startOutputCapture();
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
        courseManager.addActivitiesToCourse(course1, view);
        assertEquals(2, course1.getActivities().size(), "Two activity should have been added to the course.");
        assertTrue(course1.getActivities().get(0) instanceof Lecture, "The added activity should be a Lecture.");
        assertEquals(DayOfWeek.MONDAY, course1.getActivities().get(0).getDay(), "The day should be MONDAY.");
        assertEquals("Room 102", course1.getActivities().get(1).getLocation(), "Location should be 'Room 101'.");
        assertTrue(((Lecture) course1.getActivities().get(0)).getRecorded(), "Lecture should be marked as recorded.");
    }

    @ Test
    public void testAddActivityError() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        setMockInput(
                "1",
                "2025-04-10",
                "2025-01-10",
                "2025-04-11",
                "09:00",
                "08:00",
                "10:00",
                "Room 101",
                "chewsday",
                "MONDAY",
                "extended_extra_session",
                "lecture",
                "not true",
                "true"
        );
        AdminStaffController adminStaffController = new AdminStaffController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        CourseInfo newCourseInfoBasic = new CourseInfo();

        CourseManager courseManager = context.getCourseManager();
        startOutputCapture();
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

        new TextUserInterface().displayInfo("Starting addActivitiesToCourse");
        courseManager.addActivitiesToCourse(course1, new TextUserInterface());
        assertEquals(1, course1.getActivities().size(), "One activity should have been added to the course.");
        assertTrue(course1.getActivities().get(0) instanceof Lecture, "The added activity should be a Lecture.");
        assertEquals(DayOfWeek.MONDAY, course1.getActivities().get(0).getDay(), "The day should be MONDAY.");
        assertEquals("Room 101", course1.getActivities().get(0).getLocation(), "Location should be 'Room 101'.");
        assertTrue(((Lecture) course1.getActivities().get(0)).getRecorded(), "Lecture should be marked as recorded.");
    }

    @ Test
    public void testAddActivityErrorCapacity() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsAdminStaff(context);
        setMockInput(
                "1",
                "2025-04-10",
                "2025-04-11",
                "09:00",
                "10:00",
                "Room 101",
                "chewsday",
                "MONDAY",
                "extended_extra_session",
                "lecture",
                "asdfasdfw",
                "true"
        );
        AdminStaffController adminStaffController = new AdminStaffController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        CourseInfo newCourseInfoBasic = new CourseInfo();

        CourseManager courseManager = context.getCourseManager();
        startOutputCapture();
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

        new TextUserInterface().displayInfo("Starting addActivitiesToCourse");
        courseManager.addActivitiesToCourse(course1, new TextUserInterface());
        assertEquals(1, course1.getActivities().size(), "One activity should have been added to the course.");
        assertTrue(course1.getActivities().get(0) instanceof Lecture, "The added activity should be a Lecture.");
        assertEquals(DayOfWeek.MONDAY, course1.getActivities().get(0).getDay(), "The day should be MONDAY.");
        assertEquals("Room 101", course1.getActivities().get(0).getLocation(), "Location should be 'Room 101'.");
        assertTrue(((Lecture) course1.getActivities().get(0)).getRecorded(), "Lecture should be marked as recorded.");
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