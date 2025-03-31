package model;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Timetable {

    // The student's email that this timetable corresponds to
    private String studentEmail;

    // A list to store the time slots for this timetable
    private List<TimeSlot> timeSlots;

    // Constructor initializing the studentEmail and the timeSlots list
    public Timetable(String studentEmail) {
        this.studentEmail = studentEmail;
        this.timeSlots = new ArrayList<>();
    }

    /**
     * Adds a new TimeSlot to the timetable.
     *
     * @param day         the day of the week for the time slot
     * @param startDate   the start date of the time slot
     * @param startTime   the start time of the time slot
     * @param endDate     the end date of the time slot
     * @param endTime     the end time of the time slot
     * @param courseCode  the course code for the time slot
     * @param activityId  the activity ID for the time slot
     */
    public void addTimeSlot(DayOfWeek day, LocalDate startDate, LocalTime startTime,
                            LocalDate endDate, LocalTime endTime, String courseCode,
                            int activityId) {
        // When adding a new time slot, we default its status to UNCHOSEN.
        TimeSlot newSlot = new TimeSlot(day, startDate, startTime, endDate, endTime,
                courseCode, activityId, TimeSlotStatus.UNCHOSEN);

        timeSlots.add(newSlot);
    }

    /**
     * Counts how many activities for a given course are chosen.
     *
     * @param courseCode  the course code to check
     * @return the number of chosen activities for the course
     */
    public int numChosenActivities(String courseCode) {
        int count = 0;
        for (TimeSlot ts : timeSlots) {
            if (ts.hasCourseCode(courseCode) && ts.isChosen()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks for time conflicts between the given time period and the existing time slots.
     * If any existing time slot overlaps with the input period, its details are added to the conflict list.
     *
     * @param startDate the start date of the period to check
     * @param startTime the start time of the period to check
     * @param endDate   the end date of the period to check
     * @param endTime   the end time of the period to check
     * @return an array of String representations for the conflicting time slots
     */
    public String[] checkConflicts(LocalDate startDate, LocalTime startTime,
                                   LocalDate endDate, LocalTime endTime) {
        List<String> conflicts = new ArrayList<>();
        LocalDateTime inputStart = LocalDateTime.of(startDate, startTime);
        LocalDateTime inputEnd = LocalDateTime.of(endDate, endTime);

        for (TimeSlot ts : timeSlots) {
            // Assuming TimeSlot has getters for start and end date/time.
            LocalDateTime slotStart = LocalDateTime.of(ts.getStartDate(), ts.getStartTime());
            LocalDateTime slotEnd = LocalDateTime.of(ts.getEndDate(), ts.getEndTime());
            // Check if the two time intervals overlap.
            if (inputStart.isBefore(slotEnd) && slotStart.isBefore(inputEnd)) {
                conflicts.add(ts.toString());
            }
        }
        return conflicts.toArray(new String[0]);
    }

    /**
     * Checks if this timetable belongs to the student with the given email.
     *
     * @param email the email to check
     * @return true if the studentEmail matches, false otherwise
     */
    public boolean hasStudentEmail(String email) {
        return this.studentEmail.equals(email);
    }

    /**
     * Chooses an activity for a given course by setting its status to CHOSEN.
     *
     * @param courseCode the course code of the activity to choose
     * @param activityId the activity ID of the activity to choose
     * @return true if the activity was found and marked as CHOSEN, false otherwise
     */
    public boolean chooseActivity(String courseCode, int activityId) {
        for (TimeSlot ts : timeSlots) {
            if (ts.hasCourseCode(courseCode) && ts.hasActivityId(activityId)) {
                if (!ts.isChosen()) {
                    ts.setStatus(TimeSlotStatus.CHOSEN);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if there is at least one time slot for the specified course.
     *
     * @param courseCode the course code to check for
     * @return true if at least one time slot with the given course code exists, false otherwise
     */
    public boolean hasSlotsForCourse(String courseCode) {
        for (TimeSlot ts : timeSlots) {
            if (ts.hasCourseCode(courseCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes all time slots for the specified course.
     *
     * @param courseCode the course code whose time slots should be removed
     */
    public void removeSlotsForCourse(String courseCode) {
        timeSlots.removeIf(ts -> ts.hasCourseCode(courseCode));
    }

    /**
     * Provides a string representation of the timetable, including the student email
     * and details of all time slots.
     *
     * @return a string representation of the timetable
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Timetable for student: ").append(studentEmail).append("\n");
        for (TimeSlot ts : timeSlots) {
            sb.append(ts.toString()).append("\n");
        }
        return sb.toString();
    }
}
