package model;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

public class testCheckConflicts {

    private Timetable scheduler;

    @Before
    public void setUp() {
        scheduler = new Timetable("trini@gmail.com");
    }

    @Test
    public void testCheckConflicts_NoConflict() {
        // Create a time slot that does not conflict with the input.
        // Example: TimeSlot is from 10:00 to 11:00.

        scheduler.addTimeSlot(
                DayOfWeek.MONDAY,
                LocalDate.of(2025, 4, 10),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 4, 10),
                LocalTime.of(11, 0),
                "CS101",
                101
        );

        // Test with an input time that does not overlap.
        // Input from 11:30 to 12:30.
        String[] result = scheduler.checkConflicts(
                LocalDate.of(2025, 4, 10),
                LocalTime.of(11, 30),
                LocalDate.of(2025, 4, 10),
                LocalTime.of(12, 30)
        );
        assertEquals("Expected no conflict", 0, result.length);
    }

    @Test
    public void testCheckConflicts_WithConflict() {
        // Create a time slot that will conflict with the input.
        // Example: TimeSlot is from 10:00 to 12:00.

        scheduler.addTimeSlot(
                DayOfWeek.MONDAY,
                LocalDate.of(2025, 4, 10),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 4, 10),
                LocalTime.of(12, 0),
                "CS102",
                102
        );

        // Test with an input time that overlaps, e.g., from 11:00 to 13:00.
        String[] result = scheduler.checkConflicts(
                LocalDate.of(2025, 4, 10),
                LocalTime.of(11, 0),
                LocalDate.of(2025, 4, 10),
                LocalTime.of(13, 0)
        );
        assertEquals("Expected conflict array length of 2", 2, result.length);
        assertEquals("CS102", result[0]);
        assertEquals(Integer.toString(102), result[1]);
    }
}
