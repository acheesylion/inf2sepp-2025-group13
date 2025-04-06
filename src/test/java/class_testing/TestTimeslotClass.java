package class_testing;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import model.*;
import model.Lab;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTimeslotClass {
    private Lab lab1;
    private Tutorial tutorial1;
    private Lecture lecture1;

    @BeforeEach
    public void setUp() {

        lecture1 = new Lecture(
                101,
                LocalDate.of(2025, 4, 1),
                LocalTime.of(9, 0),
                LocalDate.of(2025, 4, 1),
                LocalTime.of(10, 0),
                "Lecture Hall",
                DayOfWeek.MONDAY,
                true
        );

        // Create a Lab instance:
        lab1 = new Lab(
                102,
                LocalDate.of(2025, 4, 2),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 4, 2),
                LocalTime.of(11, 0),
                "Lab Room",
                DayOfWeek.TUESDAY,
                30
        );

        // Create a Tutorial instance:
        tutorial1 = new Tutorial(
                103,
                LocalDate.of(2025, 4, 3),
                LocalTime.of(11, 0),
                LocalDate.of(2025, 4, 3),
                LocalTime.of(12, 0),
                "Tutorial Room",
                DayOfWeek.WEDNESDAY,
                20
        );

    }

    @Test
    public void testHasCourseCode() {
        TimeSlot ts = new TimeSlot(lecture1, "CS101", TimeSlotStatus.CHOSEN);
        assertTrue(ts.hasCourseCode("CS101"), "Course code should match.");
        assertFalse(ts.hasCourseCode("MATH101"), "Course code should not match.");
        assertFalse(ts.hasCourseCode(""), "Course code should not match.");
    }

    @Test
    public void testHasActivityId() {
        TimeSlot ts = new TimeSlot(lab1, "CS102", TimeSlotStatus.UNCHOSEN);
        assertTrue(ts.hasActivityId(102), "Should have matching activity ID.");
        assertFalse(ts.hasActivityId(999), "Should not match a non-existing activity ID.");
    }

    @Test
    public void testIsChosenAndSetStatus() {
        TimeSlot ts = new TimeSlot(tutorial1, "CS103", TimeSlotStatus.UNCHOSEN);
        assertFalse(ts.isChosen(), "Initially, the timeslot should not be chosen.");
        ts.setStatus(TimeSlotStatus.CHOSEN);
        assertTrue(ts.isChosen(), "After setting, the timeslot should be chosen.");
    }

    @Test
    public void testGetStartAndEndTimeAndDay() {
        TimeSlot ts = new TimeSlot(lecture1, "CS101", TimeSlotStatus.CHOSEN);
        assertEquals(LocalTime.of(9, 0), ts.getStartTime(), "Start time should match.");
        assertEquals(LocalTime.of(10, 0), ts.getEndTime(), "End time should match.");
        assertEquals(DayOfWeek.MONDAY, ts.getDay(), "Day should match.");
    }

    @Test
    public void testGetActivityIdAndCourseCode() {
        TimeSlot ts = new TimeSlot(lab1, "CS102", TimeSlotStatus.UNCHOSEN);
        assertEquals(102, ts.getActivityId(), "Activity ID should match.");
        assertEquals("CS102", ts.getCourseCode(), "Course code should match.");
    }

    @Test
    public void testActivityTypeMethods() {
        TimeSlot tsTutorial = new TimeSlot(tutorial1, "CS103", TimeSlotStatus.UNCHOSEN);
        TimeSlot tsLab = new TimeSlot(lab1, "CS102", TimeSlotStatus.UNCHOSEN);
        TimeSlot tsLecture = new TimeSlot(lecture1, "CS101", TimeSlotStatus.CHOSEN);

        // These methods check the type by using instanceof on the underlying activity.
        assertTrue(tsTutorial.isTutorial(), "Should be identified as Tutorial.");
        assertFalse(tsTutorial.isLab(), "Should not be identified as Lab.");
        assertFalse(tsTutorial.isLecture(), "Should not be identified as Lecture.");

        assertTrue(tsLab.isLab(), "Should be identified as Lab.");
        assertFalse(tsLab.isTutorial(), "Should not be identified as Tutorial.");
        assertFalse(tsLab.isLecture(), "Should not be identified as Lecture.");

        assertTrue(tsLecture.isLecture(), "Should be identified as Lecture.");
        assertFalse(tsLecture.isTutorial(), "Should not be identified as Tutorial.");
        assertFalse(tsLecture.isLab(), "Should not be identified as Lab.");
    }

    @Test
    public void testToString() {
        TimeSlot ts = new TimeSlot(lecture1, "CS101", TimeSlotStatus.CHOSEN);
        String output = ts.toString();
        assertTrue(output.contains("MONDAY"), "toString() should include the day.");
        assertTrue(output.contains("CS101"), "toString() should include the course code.");
        assertTrue(output.contains("101"), "toString() should include the activity id.");
    }

}
