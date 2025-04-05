package class_testing;
import static org.junit.Assert.*;

import model.Lab;
import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class ActivityTest {

    // Helper method to create a concrete instance
    private Lab createLab() {
        return new Lab(
                1,
                LocalDate.of(2025, 3, 24),
                LocalTime.of(10, 0),
                LocalDate.of(2025, 3, 24),
                LocalTime.of(12, 0),
                "New York",
                DayOfWeek.MONDAY,
                1
        );
    }

    @Test
    public void testHasId() {
        Lab lab = createLab();
        assertTrue(lab.hasId(1));
        assertFalse(lab.hasId(2));
    }

    @Test
    public void testToString() {
        Lab lab = createLab();
        String str = lab.toString();
        // Check that the string representation contains expected values
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("New York"));
        assertTrue(str.contains("MONDAY"));
    }

    @Test
    public void testGetCapacity() {
        Lab lab = createLab();
        int capacity = lab.getCapacity();
        assertTrue(capacity >= 1);
    }

    @Test
    public void testSetCapacity() {
        Lab lab = createLab();
        lab.setCapacity(35);
        assertEquals(35, lab.getCapacity());
    }
}