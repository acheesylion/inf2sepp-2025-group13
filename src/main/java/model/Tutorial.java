package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The Tutorial class represents a tutorial activity. It extends the Activity class and includes
 * an additional property for the tutorial's capacity. This class is used to define tutorial sessions
 * within a course or schedule.
 */
public class Tutorial extends Activity {
    private final int capacity;

    /**
     * Constructs a Tutorial object with the specified details.
     *
     * @param id         The unique identifier for the activity.
     * @param startDate  The start date of the tutorial activity.
     * @param startTime  The start time of the tutorial activity.
     * @param endDate    The end date of the tutorial activity.
     * @param endTime    The end time of the tutorial activity.
     * @param location   The location where the tutorial activity takes place.
     * @param day        The day of the week the tutorial activity occurs.
     * @param capacity   The maximum number of participants for the tutorial activity.
     */
    public Tutorial(int id, LocalDate startDate, LocalTime startTime,
                    LocalDate endDate, LocalTime endTime, String location,
                    DayOfWeek day, int capacity) {
        super(id, startDate, startTime, endDate, endTime, location, day);
        this.capacity = capacity;
    }

    /**
     * Returns a string representation of the tutorial activity, including its type and capacity,
     * along with the common details inherited from the Activity class.
     *
     * @return A formatted string representing the tutorial activity.
     */
    @Override
    public String toString() {
        String typeInfo = "Tutorial (Capacity: " + capacity + ")";
        return getCommonRow() + String.format("| %-25s |", typeInfo);
    }
}
