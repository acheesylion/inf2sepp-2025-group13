package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class Lecture extends Activity {
    private boolean recorded;

    public Lecture(int id, LocalDate startDate, LocalTime startTime,
                   LocalDate endDate, LocalTime endTime, String location,
                   DayOfWeek day, boolean recorded) {
        super(id, startDate, startTime, endDate, endTime, location, day);
        this.recorded = recorded;
    }

    public Lecture(Lecture other) {
        super(other.getId(), other.getStartDate(), other.getStartTime(),
                other.getEndDate(), other.getEndTime(), other.getLocation(), other.getDay());
        this.recorded = other.recorded;
    }

    public boolean getRecorded() {
        return (this.recorded);
    }

    public void setRecorded(boolean recorded) {
        this.recorded = recorded;
    }

    @Override
    public String toString() {
        return super.toString() + ", recorded=" + recorded;
    }

}
