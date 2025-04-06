package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

/**
 * The Lab class represents a lab activity. It extends the Activity class and includes
 * an additional property for the lab's capacity. This class is used to define lab sessions
 * within a course or schedule.
 */
public class Lab extends Activity {
    private final int capacity;

    /**
     * Constructs a Lab object with the specified details.
     *
     * @param id         The unique identifier for the activity.
     * @param startDate  The start date of the lab activity.
     * @param startTime  The start time of the lab activity.
     * @param endDate    The end date of the lab activity.
     * @param endTime    The end time of the lab activity.
     * @param location   The location where the lab activity takes place.
     * @param day        The day of the week the lab activity occurs.
     * @param capacity   The maximum number of participants for the lab activity.
     */
    public Lab(int id, LocalDate startDate, LocalTime startTime,
               LocalDate endDate, LocalTime endTime, String location,
               DayOfWeek day, int capacity) {
        super(id, startDate, startTime, endDate, endTime, location, day);
        this.capacity = capacity;
    }

    /**
     * Returns a string representation of the lab activity, including its type and capacity,
     * along with the common details inherited from the Activity class.
     *
     * @return A formatted string representing the lab activity.
     */
    @Override
    public String toString() {
        String typeInfo = "LAB (Capacity: " + capacity + ")";
        return getCommonRow() + String.format("| %-25s |", typeInfo);
    }
}
