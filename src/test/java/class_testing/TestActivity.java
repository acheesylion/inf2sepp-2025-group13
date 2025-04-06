package class_testing;

import model.Lab;
import model.Lecture;
import model.Tutorial;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestActivity {
    @Test
    public void testToStringLab() {
        Lab lab = new Lab(
                101,
                LocalDate.of(2025, 4, 10),
                LocalTime.of(9, 0),
                LocalDate.of(2025, 4, 10),
                LocalTime.of(11, 0),
                "Lab Room A",
                DayOfWeek.MONDAY,
                30
        );

        String output = lab.toString();
        assertTrue(output.contains("Lab Room A"), "toString should include the location from the Activity");
        assertTrue(output.contains("capacity=30"), "toString should include the capacity");
    }

    @Test
    public void testToStringTutorial() {
        Tutorial tutoral = new Tutorial(
                101,
                LocalDate.of(2025, 4, 10),
                LocalTime.of(9, 0),
                LocalDate.of(2025, 4, 10),
                LocalTime.of(11, 0),
                "Tutorial Room A",
                DayOfWeek.MONDAY,
                30
        );

        String output = tutoral.toString();
        assertTrue(output.contains("Tutorial Room A"), "toString should include the location from the Activity");
        assertTrue(output.contains("capacity=30"), "toString should include the capacity");
    }

    @Test
    public void testToStringLecture() {
        Lecture lecture = new Lecture(
                101,
                LocalDate.of(2025, 4, 10),
                LocalTime.of(9, 0),
                LocalDate.of(2025, 4, 10),
                LocalTime.of(11, 0),
                "Lecture Room A",
                DayOfWeek.MONDAY,
                true
        );

        String output = lecture.toString();
        assertTrue(output.contains("Lecture Room A"), "toString should include the location from the Activity");
        assertTrue(output.contains("recorded=true"), "toString should include the capacity");
    }


}
