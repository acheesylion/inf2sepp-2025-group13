package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class Tutorial extends Activity {
    private final int capacity;

    public Tutorial(int id, LocalDate startDate, LocalTime startTime,
                    LocalDate endDate, LocalTime endTime, String location,
                    DayOfWeek day, int capacity) {
        super(id, startDate, startTime, endDate, endTime, location, day);
        this.capacity = capacity;
    }

    public Tutorial(Tutorial other) {
        super(other.getId(), other.getStartDate(), other.getStartTime(),
                other.getEndDate(), other.getEndTime(), other.getLocation(), other.getDay());
        this.capacity = other.capacity;
    }

    @Override
    public String toString() {
        String typeInfo = "Tutorial (Capacity: " + capacity + ")";
        return getCommonRow() + String.format("| %-25s |", typeInfo);
    }

}
