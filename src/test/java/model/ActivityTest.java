package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestActivity extends Activity{
    public TestActivity(int id, LocalDate startDate, LocalTime startTime,
                        LocalDate endDate, LocalTime endTime, String location,
                        DayOfWeek day) {
        super(id, startDate, startTime, endDate, endTime, location, day);

    }
}

public class ActivityTest{
    private TestActivity activity;

    @BeforeEach
    void setUp(){
        activity = new TestActivity(
                1,
                LocalDate.of(2025,4,3),
                LocalTime.of(9,0),
                LocalDate.of(2025,4,3),
                LocalTime.of(10,0),
                "Room 101",
                DayOfWeek.THURSDAY
        );
    }

    @Test
    void testGetters(){
        assertEquals(1, activity.getId());
        assertEquals(LocalDate.of(2025, 4, 3), activity.getStartDate());
        assertEquals(LocalTime.of(9, 0), activity.getStartTime());
        assertEquals(LocalDate.of(2025, 4, 3), activity.getEndDate());
        assertEquals(LocalTime.of(10, 0), activity.getEndTime());
        assertEquals("Room 101", activity.getLocation());
        assertEquals(DayOfWeek.THURSDAY, activity.getDay());
    }

    @Test
    void testHasId() {
        assertTrue(activity.hasId(1));
        assertFalse(activity.hasId(2));
    }

    @Test
    void testToString() {
        String expected = "Activity{" +
                "id=" + 1 +
                ", startDate=" + LocalDate.of(2025, 4, 3) +
                ", startTime=" + LocalTime.of(9, 0) +
                ", endDate=" + LocalDate.of(2025, 4, 3) +
                ", endTime=" + LocalTime.of(10, 0) +
                ", location='" + "Room 101" + '\'' +
                ", day=" + DayOfWeek.THURSDAY +
                '}';
        assertEquals(expected, activity.toString());
    }
}

