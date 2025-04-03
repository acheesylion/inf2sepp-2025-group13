package model;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Timetable {

    private final String studentEmail;
    private List<TimeSlot> timeSlots;

    // Constructor initializing the studentEmail and the timeSlots list
    public Timetable(String studentEmail) {
        this.studentEmail = studentEmail;
        this.timeSlots = new ArrayList<>();
    }



    public void addTimeSlot(Activity activity, String courseCode) {
        // When adding a new time slot, we default its status to UNCHOSEN.

        TimeSlot newSlot = null;
        if (activity instanceof Lab) {
            newSlot = new TimeSlot(activity, courseCode, TimeSlotStatus.CHOSEN);
        } else {
            newSlot = new TimeSlot(activity, courseCode, TimeSlotStatus.UNCHOSEN);
        }
        timeSlots.add(newSlot);
    }

    public TimeSlot getTimeSlotByActivityId(int activityId) {
        return timeSlots.stream()
                .filter(ts -> ts.getActivity().hasId(activityId))
                .findFirst()
                .orElse(null);
    }


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
     * Checks if there is a conflict between the specified time interval and any existing time slots.
     *
     * <p>
     * The method iterates over all {@code TimeSlot} objects stored in {@code timeSlots} and compares their
     * intervals with the provided start and end dates/times. If an overlapping interval is found,
     * it returns an array containing the course code and activity ID of the conflicting time slot.
     * Otherwise, an empty array is returned.
     * </p>
     *
     * @param startDate the start date of the time interval to check
     * @param startTime the start time of the time interval to check
     * @param endDate   the end date of the time interval to check
     * @param endTime   the end time of the time interval to check
     * @return an array of two strings: the first element is the course code and the second element is the activity ID
     *         of the conflicting time slot, or an empty array if no conflict is found.
     */
    public String[] checkConflicts(LocalDate startDate, LocalTime startTime,
                                   LocalDate endDate, LocalTime endTime) {
        LocalDateTime inputStart = LocalDateTime.of(startDate, startTime);
        LocalDateTime inputEnd = LocalDateTime.of(endDate, endTime);

        for (TimeSlot ts : timeSlots) {
            LocalDateTime slotStart = LocalDateTime.of(ts.getStartDate(), ts.getStartTime());
            LocalDateTime slotEnd = LocalDateTime.of(ts.getEndDate(), ts.getEndTime());

            if (inputStart.isBefore(slotEnd) && slotStart.isBefore(inputEnd)) {
                String conflictCourseCode = ts.getCourseCode();
                String conflictID = Integer.toString(ts.getActivityId());
                return (new String[] {conflictCourseCode, conflictID});
            }
        }

        // Return an empty array if no conflicts are found.
        return (new String[0]);
    }


    public boolean hasStudentEmail(String email) {return (this.studentEmail.equals(email));}

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

    public boolean hasSlotsForCourse(String courseCode) {

        if (timeSlots == null) {
            return false;
        }

        for (TimeSlot ts : timeSlots) {
            if (ts.hasCourseCode(courseCode)) {
                return true;
            }
        }
        return false;
    }

    public void removeSlotsForCourse(String courseCode) {
        timeSlots.removeIf(ts -> ts.hasCourseCode(courseCode));
    }

    public int numTutorialInTimeSlots(String courseCode) {
        return (int) timeSlots.stream()
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.getActivity() instanceof Tutorial)
                .count();
    }

    public int numLabInTimeSlots(String courseCode) {
        return (int) timeSlots.stream()
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.getActivity() instanceof Lab)
                .count();
    }


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
