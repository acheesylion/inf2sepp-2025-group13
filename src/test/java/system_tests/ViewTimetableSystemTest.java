package system_tests;

import controller.AdminStaffController;
import controller.GuestController;
import controller.StudentController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


public class ViewTimetableSystemTest extends TUITest {
    private Timetable timetable;
    private Timetable nullTimetable;

    @BeforeEach
    public void setUp() {
        // Create a Timetable for a specific student
        timetable = new Timetable("student1@hindeburg.ac.uk");

        // Create a Lecture, LAB, and Tutorial with different properties.
        Lecture lectureActivity = new Lecture(
                101,
                LocalDate.of(2025, 4, 1),
                LocalTime.of(9, 0),
                LocalDate.of(2025, 4, 1),
                LocalTime.of(10, 0),
                "Lecture Hall",
                DayOfWeek.MONDAY,
                true
        );

        Lab labActivity = new Lab(
                102,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(11, 0),
                "LAB Room",
                DayOfWeek.TUESDAY,
                30
        );

        Tutorial tutorialActivity = new Tutorial(
                103,
                LocalDate.of(2025, 4, 3),
                LocalTime.of(11, 0),
                LocalDate.of(2025, 4, 3),
                LocalTime.of(12, 0),
                "Tutorial Room",
                DayOfWeek.WEDNESDAY,
                10
        );

        timetable.addTimeSlot(lectureActivity, "MATH10101", ActivityType.LAB);
        timetable.addTimeSlot(tutorialActivity, "MATH10101", ActivityType.LAB);
        timetable.addTimeSlot(labActivity, "MATH10101", ActivityType.LAB);
        timetable.chooseActivity("MATH10101", tutorialActivity.getId());
        timetable.chooseActivity("MATH10101", lectureActivity.getId());

        nullTimetable  = new Timetable("student3@hindeburg.ac.uk");
    }
    @Test
    public void viewTimetable() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsStudent1(context);
        setMockInput(
                "5", "2", "-1", "-1"
        );
        StudentController studentController = new StudentController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        CourseManager courseManager = context.getCourseManager();
        courseManager.timetables.add(timetable);
        courseManager.timetables.add(nullTimetable);
        studentController.manageTimetable();
        assertOutputContains("SUCCESS");
    }

    @Test
    public void viewTimetableEmpty() throws URISyntaxException, IOException, ParseException {
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        loginAsStudent2(context);
        setMockInput(
                "5", "2", "-1", "-1"
        );
        StudentController studentController = new StudentController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        CourseManager courseManager = context.getCourseManager();
        courseManager.timetables.add(timetable);
        courseManager.timetables.add(nullTimetable);
        studentController.manageTimetable();
        assertOutputContains("Timetable does not exist");
    }
}