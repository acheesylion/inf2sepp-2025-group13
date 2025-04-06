package system_tests;

import controller.InquirerController;
import controller.MenuController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.Course;
import model.CourseManager;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class TestFromMainMenu extends TUITest {
    public void populateCourseList(CourseManager courseManager) {
        Course course1 = new Course(
                "COMS10101",
                "Introduction to Computer Science",
                "Learn the fundamentals of computer science and programming.",
                false,
                "Alice Johnson",
                "teacher1@hindeburg.ac.uk",
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
                DayOfWeek.TUESDAY,
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
                false,         // lecture
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
    @Test
    public void testFullMainMenu() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Hello! What would you like to do?");

    }

    @Test
    public void testFullLogInAdmin() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0", "admin1", "admin1pass",
                "0", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Logged in as admin1");
    }

    @Test
    public void testFullLogInStudent() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0", "student1", "student1pass",
                "0", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Logged in as student1");
    }

    @Test
    public void testFullLogInTeaching() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0", "teacher1", "teacher1pass",
                "0", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Logged in as teacher1");
    }

    @Test
    public void testFullLogInTeachingManagedReceived() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0", "teacher1", "teacher1pass",
                "1",
                "0", "Testing response",
                "-1",
                "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        InquirerController inquirerController = new InquirerController(context, view, new MockAuthenticationService(), new MockEmailService());
        inquirerController.sendInquiry("email@email", "Issue with assignment", "i am unable to submit assignment", true, "COMS10101" );
        System.out.println((context.inquiries).size());
        MenuController menuController = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menuController.mainMenu();
        assertOutputContains("Logged in as teacher1");
    }


}
