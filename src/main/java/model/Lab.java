package model;
import javax.management.relation.Role;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class Lab extends Activity {
    private int capacity;

    public Lab(int id, LocalDate startDate, LocalTime startTime,
               LocalDate endDate, LocalTime endTime, String location,
               DayOfWeek day, int capacity) {
        super(id, startDate, startTime, endDate, endTime, location, day);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return super.toString() + ", capacity=" + capacity;
    }

}
