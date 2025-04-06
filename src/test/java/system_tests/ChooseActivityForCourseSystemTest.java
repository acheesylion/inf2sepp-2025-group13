package system_tests;

import controller.StudentController;
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


public class ChooseActivityForCourseSystemTest extends TUITest {
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
                2
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
                1234,
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
                1244,
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
                1111,
                LocalDate.of(2025, 4, 13),
                LocalTime.of(8, 0),
                LocalDate.of(2025, 4, 13),
                LocalTime.of(9, 0),
                "Lecture Hall 2",
                DayOfWeek.THURSDAY,
                20,         // lecture
                "lab"
        );
        course2.addActivity(
                5432,
                LocalDate.of(2025, 4, 14),
                LocalTime.of(9, 0),
                LocalDate.of(2025, 4, 14),
                LocalTime.of(10, 0),
                "Tutorial Room 2",
                DayOfWeek.TUESDAY,
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
                "Physics Lab",
                DayOfWeek.TUESDAY,
                25,           // capacity for lab
                "lab"
        );

        courseManager.addCourseToCourseList(course1);
        courseManager.addCourseToCourseList(course2);
        courseManager.addCourseToCourseList(course3);
    }

    @Test
    public void testFullChooseActivityForCourse() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "COMS10101",
                "3",
                "COMS10101",
                "1234",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsStudent1(context);
        StudentController studentController = new StudentController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        studentController.manageTimetable();
        assertOutputContains("The activity was successfully added to your timetable");
    }

    @Test
    public void testNotIntegerChooseActivity() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "COMS10101",
                "3",
                "COMS10101",
                "asdfsaf",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsStudent1(context);
        StudentController studentController = new StudentController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        studentController.manageTimetable();
        assertOutputContains("Incorrect activity id");

    }

    @Test
    public void testTimetableExistChooseActivity() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "3",
                "COMS10101",
                "1234",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsStudent1(context);
        StudentController studentController = new StudentController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        studentController.manageTimetable();
        assertOutputContains("Timetable does not exist");
    }

    @Test
    public void testIncorrectCodeChooseActivity() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "COMS10101",
                "3",
                "asdfasdfsa",
                "1234",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsStudent1(context);
        StudentController studentController = new StudentController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        studentController.manageTimetable();
        assertOutputContains("Incorrect course code");
    }

    @Test
    public void testIncorrectActivityIdChooseActivity() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "COMS10101",
                "3",
                "COMS10101",
                "123",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsStudent1(context);
        StudentController studentController = new StudentController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        studentController.manageTimetable();
        assertOutputContains("Activity does not exist in timetable");
    }

    @Test
    public void testCourseNotInTimetableChooseActivity() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "COMS10101",
                "3",
                "MATH20120",
                "1234",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsStudent1(context);
        StudentController studentController = new StudentController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        studentController.manageTimetable();
        assertOutputContains("Course does not exist in timetable");
    }

    @Test
    public void testConflictTutorialChooseActivity() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "COMS10101",
                "0",
                "MATH20120",
                "3",
                "MATH20120",
                "5432",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsStudent1(context);
        StudentController studentController = new StudentController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        studentController.manageTimetable();
        assertOutputContains("The activity was successfully added to your timetable");

    }

    @Test
    public void testChooseTutorialChooseActivity() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "COMS10101",
                "3",
                "COMS10101",
                "1244",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsStudent1(context);
        StudentController studentController = new StudentController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        studentController.manageTimetable();
        assertOutputContains("You have to choose 1 more tutorial(s) for this course");

    }

    @Test
    public void testChooseLabChooseActivity() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "COMS10101",
                "3",
                "COMS10101",
                "1234",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsStudent1(context);
        StudentController studentController = new StudentController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        studentController.manageTimetable();
        assertOutputContains("You have to choose 1 more lab(s) for this course");

    }

    @Test
    public void testChooseLabNegativeChooseActivity() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH20120",
                "3",
                "MATH20120",
                "1111",
                "-1", "-1"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        CourseManager courseManager = context.getCourseManager();
        populateCourseList(courseManager);
        loginAsStudent1(context);
        StudentController studentController = new StudentController(context, view, new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        studentController.manageTimetable();
        assertOutputContains("You have to choose 1 less lab(s) for this course");

    }



}