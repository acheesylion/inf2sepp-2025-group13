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

    public Lab(Lab other) {
        super(other.getId(), other.getStartDate(), other.getStartTime(),
                other.getEndDate(), other.getEndTime(), other.getLocation(), other.getDay());
        this.capacity = other.capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return super.toString() + ", capacity=" + capacity;
    }

}
