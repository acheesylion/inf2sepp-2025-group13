package system_tests;

import controller.MenuController;
import controller.ViewerController;
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


public class ViewCoursesSystemTest extends TUITest {
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

    @Test
    public void testCoursesExists() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        ViewerController viewerController = new ViewerController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        viewerController.viewCourses();
        assertOutputContains("SUCCESS");
    }

    @ Test
    public void testCoursesDoesNotExist() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        ViewerController viewerController = new ViewerController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        viewerController.viewCourses();
        assertOutputContains("No courses found");
    }

    @ Test
    public void testCorrectViewCourseFromMainMenuController() throws URISyntaxException, IOException, ParseException {
        setMockInput("3", "-1");
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        MenuController menus = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menus.mainMenu();
        assertOutputContains("SUCCESS");

    }
    @ Test
    public void testViewIncorrectCourseFromMainMenuController() throws URISyntaxException, IOException, ParseException {
        setMockInput("3", "-1");
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        MenuController menus = new MenuController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        menus.mainMenu();
        assertOutputContains("No courses found");

    }

}