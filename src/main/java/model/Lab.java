package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class Lab extends Activity {
    private final int capacity;

    public Lab(int id, LocalDate startDate, LocalTime startTime,
               LocalDate endDate, LocalTime endTime, String location,
               DayOfWeek day, int capacity) {
        super(id, startDate, startTime, endDate, endTime, location, day);
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        String typeInfo = "LAB (Capacity: " + capacity + ")";
        return getCommonRow() + String.format("| %-25s |", typeInfo);
    }

}
