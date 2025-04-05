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
    private Lecture lectureActivityClash1;
    private Lab labActivityClash1;
    private Tutorial tutorialActivityClash1;

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


        lectureActivityClash1 = new Lecture(
                101,
                LocalDate.of(2025, 4, 1),
                LocalTime.of(9, 30),
                LocalDate.of(2025, 4, 1),
                LocalTime.of(10, 0),
                "Lecture Hall",
                DayOfWeek.WEDNESDAY,
                true
        );
        tutorialActivityClash1 = new Tutorial(
                103,
                LocalDate.of(2025, 4, 3),
                LocalTime.of(8, 30),
                LocalDate.of(2025, 4, 3),
                LocalTime.of(9, 0),
                "Tutorial Room",
                DayOfWeek.WEDNESDAY,
                10
        );

        labActivityClash1 = new Lab(
                102,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(7, 0),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(11, 0),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );




    }

    @Test
    public void testHasSlotsForCourse() {
        //Empty Timeslot list
        String courseCode = "MATH12345";
        assertFalse(timetable.hasSlotsForCourse(courseCode));

        // Populated Timeslot list
        timetable.addTimeSlot(lectureActivity, "MATH12345");
        timetable.addTimeSlot(labActivity, "MATH12345");
        timetable.addTimeSlot(tutorialActivity, "MATH12345");
        assertTrue(timetable.hasSlotsForCourse(courseCode));
        assertFalse(timetable.hasSlotsForCourse("INFD12345"));

    }

    @Test
    public void testHasSlotsForActivity() {
        //Empty Timeslot list
        int activityId = 101;
        assertFalse(timetable.hasSlotsForActivityId(activityId));

        // Populated Timeslot list
        timetable.addTimeSlot(lectureActivity, "MATH12345");
        timetable.addTimeSlot(labActivity, "MATH12345");
        timetable.addTimeSlot(tutorialActivity, "MATH12345");
        assertTrue(timetable.hasSlotsForActivityId(activityId));
        int randomActivityId = 123;
        assertFalse(timetable.hasSlotsForActivityId(randomActivityId));

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
    @Test
    public void testCheckConflictsPriorityLecture() {
        // Tutorial at 8.30 - 9:00, Lecture at 9:30 - 10:00
        // Lab at 7:00 - 11:00
        // Clashes with both tutorial and lecture
        // Only returns the lectures because of higher priority
        timetable.addTimeSlot(tutorialActivityClash1, "CS103");
        timetable.addTimeSlot(lectureActivityClash1, "CS102");
        timetable.addTimeSlot(labActivityClash1, "CS103");
        timetable.chooseActivity("CS103", tutorialActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash1.getDay(),
                labActivityClash1.getStartTime(),
                labActivityClash1.getEndTime()
        );
        assertTrue(conflicts.length > 0);
    }
    @Test
    public void testCheckConflictsSameStartSameEndPeriod() {
        Lab labActivityClash2 = new Lab(
                222,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(7, 0),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(11, 0),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );
        timetable.addTimeSlot(labActivityClash1, "MATH1232");
        timetable.addTimeSlot(labActivityClash2, "MATH1232");
        timetable.chooseActivity("MATH1232", labActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash2.getDay(),
                labActivityClash2.getStartTime(),
                labActivityClash2.getEndTime()
        );
        assertTrue(conflicts.length > 0);
    }
    @Test
    public void testCheckConflictsLateStartLateEndPeriod() {
        Lab labActivityClash2 = new Lab(
                222,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(7, 30),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(11, 30),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );
        timetable.addTimeSlot(labActivityClash1, "MATH1232");
        timetable.addTimeSlot(labActivityClash2, "MATH1232");
        timetable.chooseActivity("MATH1232", labActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash2.getDay(),
                labActivityClash2.getStartTime(),
                labActivityClash2.getEndTime()
        );
        assertTrue(conflicts.length > 0);
    }
    @Test
    public void testCheckConflictsEarlyStartEarlyEndPeriod() {
        Lab labActivityClash2 = new Lab(
                222,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(6, 30),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(10, 30),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );
        timetable.addTimeSlot(labActivityClash1, "MATH1232");
        timetable.addTimeSlot(labActivityClash2, "MATH1232");
        timetable.chooseActivity("MATH1232", labActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash2.getDay(),
                labActivityClash2.getStartTime(),
                labActivityClash2.getEndTime()
        );
        assertTrue(conflicts.length > 0);
    }
    @Test
    public void testCheckConflictsEarlyStartLateEndPeriod() {
        Lab labActivityClash2 = new Lab(
                102,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(6, 30),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(11, 30),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );
        timetable.addTimeSlot(labActivityClash1, "MATH1232");
        timetable.addTimeSlot(labActivityClash2, "MATH1232");
        timetable.chooseActivity("MATH1232", labActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash2.getDay(),
                labActivityClash2.getStartTime(),
                labActivityClash2.getEndTime()
        );
        assertTrue(conflicts.length > 0);
    }
    @Test
    public void testCheckConflictsLateStartEarlyEndPeriod() {
        Lab labActivityClash2 = new Lab(
                222,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(8, 30),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(10, 30),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );
        timetable.addTimeSlot(labActivityClash1, "MATH1232");
        timetable.addTimeSlot(labActivityClash2, "MATH1232");
        timetable.chooseActivity("MATH1232", labActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash2.getDay(),
                labActivityClash2.getStartTime(),
                labActivityClash2.getEndTime()
        );
        assertTrue(conflicts.length > 0);
    }
    @Test
    public void testCheckConflictsSameStartEarlyEndPeriod() {
        Lab labActivityClash2 = new Lab(
                222,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(7, 0),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(10, 30),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );
        timetable.addTimeSlot(labActivityClash1, "MATH1232");
        timetable.addTimeSlot(labActivityClash2, "MATH1232");
        timetable.chooseActivity("MATH1232", labActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash2.getDay(),
                labActivityClash2.getStartTime(),
                labActivityClash2.getEndTime()
        );
        assertTrue(conflicts.length > 0);
    }
    @Test
    public void testCheckConflictsSameStartLateEndPeriod() {
        Lab labActivityClash2 = new Lab(
                222,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(7, 0),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(11, 30),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );
        timetable.addTimeSlot(labActivityClash1, "MATH1232");
        timetable.addTimeSlot(labActivityClash2, "MATH1232");
        timetable.chooseActivity("MATH1232", labActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash2.getDay(),
                labActivityClash2.getStartTime(),
                labActivityClash2.getEndTime()
        );
        assertTrue(conflicts.length > 0);
    }
    @Test
    public void testCheckConflictsEarlyStartSameEndPeriod() {
        Lab labActivityClash2 = new Lab(
                222,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(6, 0),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(11, 0),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );
        timetable.addTimeSlot(labActivityClash1, "MATH1232");
        timetable.addTimeSlot(labActivityClash2, "MATH1232");
        timetable.chooseActivity("MATH1232", labActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash2.getDay(),
                labActivityClash2.getStartTime(),
                labActivityClash2.getEndTime()
        );
        assertTrue(conflicts.length > 0);
    }
    @Test
    public void testCheckConflictsLateStartSameEndPeriod() {
        Lab labActivityClash2 = new Lab(
                222,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(11, 0),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );
        timetable.addTimeSlot(labActivityClash1, "MATH1232");
        timetable.addTimeSlot(labActivityClash2, "MATH1232");
        timetable.chooseActivity("MATH1232", labActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash2.getDay(),
                labActivityClash2.getStartTime(),
                labActivityClash2.getEndTime()
        );
        assertTrue(conflicts.length > 0);
    }
    @Test
    public void testCheckConflictsSameDayNoClash() {
        Lab labActivityClash2 = new Lab(
                222,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(20, 0),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(20, 30),
                "Lab Room",
                DayOfWeek.WEDNESDAY,
                30
        );
        timetable.addTimeSlot(labActivityClash1, "MATH1232");
        timetable.addTimeSlot(labActivityClash2, "MATH1232");
        timetable.chooseActivity("MATH1232", labActivityClash1.getId());
        String[] conflicts = timetable.checkConflicts(
                labActivityClash2.getDay(),
                labActivityClash2.getStartTime(),
                labActivityClash2.getEndTime()
        );
        assertEquals(0, conflicts.length);
    }


}
