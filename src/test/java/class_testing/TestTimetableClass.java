package class_testing;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestTimetableClass {
    private Timetable timetable;
    private Lecture lectureActivity;
    private Lab labActivity;
    private Tutorial tutorialActivity;

    @BeforeEach
    public void setUp() {
        // Create a Timetable for a specific student
        timetable = new Timetable("student@example.com");

        // Create a Lecture, Lab, and Tutorial with different properties.
        lectureActivity = new Lecture(
                101,
                LocalDate.of(2025, 4, 1),
                LocalTime.of(9, 0),
                LocalDate.of(2025, 4, 1),
                LocalTime.of(10, 0),
                "Lecture Hall",
                DayOfWeek.MONDAY,
                true
        );

        labActivity = new Lab(
                102,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(11, 0),
                "Lab Room",
                DayOfWeek.TUESDAY,
                30
        );

        tutorialActivity = new Tutorial(
                103,
                LocalDate.of(2025, 4, 3),
                LocalTime.of(11, 0),
                LocalDate.of(2025, 4, 3),
                LocalTime.of(12, 0),
                "Tutorial Room",
                DayOfWeek.WEDNESDAY,
                10
        );


    }

    @Test
    public void testTimetableToStringContainsHeader() {
        timetable.addTimeSlot(lectureActivity, "CS101");
        timetable.addTimeSlot(labActivity, "CS102");
        timetable.chooseActivity("CS102", labActivity.getId());
        timetable.addTimeSlot(tutorialActivity, "CS103");
        timetable.chooseActivity("CS103", tutorialActivity.getId());
        String output = timetable.toString();
        // Check that the header is printed and the student email is centered.
        assertTrue(output.contains("Timetable for student@example.com"), "Header should contain the student email.");
        assertTrue(output.contains("======================================="), "Header separator should be present.");
    }

    @Test
    public void testTimetableToStringContainsColumnLegend() {
        timetable.addTimeSlot(lectureActivity, "CS101");
        timetable.addTimeSlot(labActivity, "CS102");
        timetable.chooseActivity("CS102", labActivity.getId());
        timetable.addTimeSlot(tutorialActivity, "CS103");
        timetable.chooseActivity("CS103", tutorialActivity.getId());
        String output = timetable.toString();
        // Verify that the legend (column headers) is printed.
        assertTrue(output.contains("Day"), "Output should contain the 'Day' header.");
        assertTrue(output.contains("Time"), "Output should contain the 'Time' header.");
        assertTrue(output.contains("CourseCode"), "Output should contain the 'CourseCode' header.");
        assertTrue(output.contains("ActivityId"), "Output should contain the 'ActivityId' header.");
        assertTrue(output.contains("Type"), "Output should contain the 'Type' header.");
    }

    @Test
    public void testTimetableToStringContainsTimeslotDetails() {
        timetable.addTimeSlot(lectureActivity, "CS101");
        timetable.addTimeSlot(labActivity, "CS102");
        timetable.chooseActivity("CS102", labActivity.getId());
        timetable.addTimeSlot(tutorialActivity, "CS103");
        timetable.chooseActivity("CS103", tutorialActivity.getId());
        String output = timetable.toString();

        // Check for known timeslot values from our test data.
        // For Monday (Lecture)
        assertTrue(output.contains("MONDAY"), "Output should contain 'MONDAY'.");
        assertTrue(output.contains("09:00 - 10:00"), "Output should contain the lecture time range.");
        assertTrue(output.contains("CS101"), "Output should contain the lecture course code.");
        assertTrue(output.contains("101"), "Output should contain the lecture activity id.");
        assertTrue(output.contains("Lecture"), "Output should indicate 'Lecture' type.");

        // For Tuesday (Lab)
        assertTrue(output.contains("TUESDAY"), "Output should contain 'TUESDAY'.");
        assertTrue(output.contains("10:00 - 11:00"), "Output should contain the lab time range.");
        assertTrue(output.contains("CS102"), "Output should contain the lab course code.");
        assertTrue(output.contains("102"), "Output should contain the lab activity id.");
        assertTrue(output.contains("Lab"), "Output should indicate 'Lab' type.");

        // For Wednesday (Tutorial)
        assertTrue(output.contains("WEDNESDAY"), "Output should contain 'WEDNESDAY'.");
        assertTrue(output.contains("11:00 - 12:00"), "Output should contain the tutorial time range.");
        assertTrue(output.contains("CS103"), "Output should contain the tutorial course code.");
        assertTrue(output.contains("103"), "Output should contain the tutorial activity id.");
        assertTrue(output.contains("Tutorial"), "Output should indicate 'Tutorial' type.");
    }
}
