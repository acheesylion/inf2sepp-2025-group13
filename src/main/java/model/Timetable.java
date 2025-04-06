package model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Timetable class represents a student's timetable, which includes a list of time slots
 * for their scheduled activities (lectures, labs, tutorials, etc.). It provides methods for adding
 * time slots, checking for conflicts, and displaying the timetable.
 */
public class Timetable {

    private final String studentEmail;
    private final List<TimeSlot> timeSlots;

    /**
     * Constructs a Timetable object for the specified student.
     *
     * @param studentEmail The email address of the student whose timetable is being created.
     */
    public Timetable(String studentEmail) {
        this.studentEmail = studentEmail;
        this.timeSlots = new ArrayList<>();
    }

    /**
     * Adds a time slot for a specific activity to the student's timetable.
     * The status of the time slot is set to `UNCHOSEN` for labs and tutorials, and `CHOSEN` for lectures.
     *
     * @param activity  The activity associated with the time slot (e.g., a lecture, lab, or tutorial).
     * @param courseCode The course code for the activity.
     * @param type      The type of activity (e.g., `LECTURE`, `LAB`, `TUTORIAL`).
     */
    public void addTimeSlot(Activity activity, String courseCode, ActivityType type) {
        if (type == ActivityType.LECTURE) {
            TimeSlot newSlot = new TimeSlot(
                    activity.getId(),
                    activity.getStartTime(), activity.getEndTime(),
                    activity.getDay(), ActivityType.LECTURE,
                    courseCode, TimeSlotStatus.CHOSEN);
            timeSlots.add(newSlot);
        }
        if (type == ActivityType.LAB) {
            TimeSlot newSlot = new TimeSlot(
                    activity.getId(),
                    activity.getStartTime(), activity.getEndTime(),
                    activity.getDay(), ActivityType.LAB,
                    courseCode, TimeSlotStatus.UNCHOSEN);
            timeSlots.add(newSlot);
        }

        if (type == ActivityType.TUTORIAL) {
            TimeSlot newSlot = new TimeSlot(
                    activity.getId(),
                    activity.getStartTime(), activity.getEndTime(),
                    activity.getDay(), ActivityType.TUTORIAL,
                    courseCode, TimeSlotStatus.UNCHOSEN);
            timeSlots.add(newSlot);
        }
    }

    /**
     * Checks if the timetable belongs to the specified student.
     *
     * @param email The email of the student to check.
     * @return true if the timetable belongs to the specified student, false otherwise.
     */
    public boolean hasStudentEmail(String email) {
        return (this.studentEmail.equals(email));
    }

    /**
     * Checks for any conflicts in the timetable based on the provided day and time range.
     * A conflict occurs if an activity overlaps with another chosen activity.
     *
     * @param day       The day of the week the activity is scheduled for.
     * @param startTime The start time of the activity.
     * @param endTime   The end time of the activity.
     * @return A string array containing the course code and activity ID of the conflicting activity, or an empty array if no conflict is found.
     */
    public String[] checkConflicts(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        List<TimeSlot> conflicts = new ArrayList<>();
        for (TimeSlot ts : timeSlots) {
            if (ts.isChosen()) {
                if (ts.getDay().equals(day)) {
                    if (startTime.isBefore(ts.getEndTime()) && endTime.isAfter(ts.getStartTime())) {
                        conflicts.add(ts);
                    }
                }
            }
        }
        if (conflicts.isEmpty()) {
            return new String[0];
        } else {
            for (TimeSlot conflict : conflicts) {
                if (conflict.isType(ActivityType.LECTURE)) {
                    String conflictCourseCode = conflict.getCourseCode();
                    int conflictID = conflict.getActivityId();
                    return new String[] {conflictCourseCode, Integer.toString(conflictID)};
                }
            }
            // Return the first conflict if no lecture is found
            String conflictCourseCode = conflicts.get(0).getCourseCode();
            int conflictID = conflicts.get(0).getActivityId();
            return new String[] {conflictCourseCode, Integer.toString(conflictID)};
        }
    }

    /**
     * Marks a specific activity as chosen for a given course.
     *
     * @param courseCode The course code for the activity.
     * @param activityId The activity ID to mark as chosen.
     */
    public void chooseActivity(String courseCode, int activityId) {
        timeSlots.stream()
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.hasActivityIdTimeSlot(activityId) && !ts.isChosen())
                .forEach(ts -> ts.setStatus(TimeSlotStatus.CHOSEN));
    }

    /**
     * Checks if the timetable contains a tutorial with the given activity ID.
     *
     * @param activityId The activity ID to check.
     * @return true if the timetable contains the tutorial, false otherwise.
     */
    public boolean isIdTutorial(int activityId) {
        return timeSlots.stream()
                .filter(ts -> ts.hasActivityIdTimeSlot(activityId))
                .anyMatch(ts -> ts.isType(ActivityType.TUTORIAL));
    }

    /**
     * Checks if the timetable contains a lab with the given activity ID.
     *
     * @param activityId The activity ID to check.
     * @return true if the timetable contains the lab, false otherwise.
     */
    public boolean isIdLab(int activityId) {
        return timeSlots.stream()
                .filter(ts -> ts.hasActivityIdTimeSlot(activityId))
                .anyMatch(ts -> ts.isType(ActivityType.LAB));
    }

    /**
     * Checks if the timetable contains any time slot for a specific course.
     *
     * @param courseCode The course code to check.
     * @return true if the timetable contains a time slot for the given course, false otherwise.
     */
    public boolean hasSlotsForCourse(String courseCode) {
        return timeSlots.stream().anyMatch(ts -> ts.hasCourseCode(courseCode));
    }

    /**
     * Checks if the timetable contains any time slot with the given activity ID.
     *
     * @param activityId The activity ID to check.
     * @return true if the timetable contains a time slot with the given activity ID, false otherwise.
     */
    public boolean hasSlotsForActivityId(int activityId) {
        return timeSlots.stream().anyMatch(ts -> ts.hasActivityIdTimeSlot(activityId));
    }

    /**
     * Removes all time slots associated with a specific course from the timetable.
     *
     * @param courseCode The course code to remove time slots for.
     */
    public void removeSlotsForCourse(String courseCode) {
        timeSlots.removeIf(ts -> ts.hasCourseCode(courseCode));
    }

    /**
     * Counts the number of chosen tutorials for a given course in the timetable.
     *
     * @param courseCode The course code to check.
     * @return The number of chosen tutorials for the course.
     */
    public int numChosenTutorialInTimeSlots(String courseCode) {
        return (int) timeSlots.stream()
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.isType(ActivityType.TUTORIAL) && ts.isChosen())
                .count();
    }

    /**
     * Counts the number of chosen labs for a given course in the timetable.
     *
     * @param courseCode The course code to check.
     * @return The number of chosen labs for the course.
     */
    public int numChosenLabInTimeSlots(String courseCode) {
        return (int) timeSlots.stream()
                .filter(ts -> ts.hasCourseCode(courseCode) && ts.isType(ActivityType.LAB) && ts.isChosen())
                .count();
    }

    /**
     * Provides a string representation of the student's timetable.
     *
     * @return A formatted string representing the student's timetable with all chosen time slots.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================\n");
        sb.append(String.format("Timetable for %s\n", studentEmail));
        sb.append("========================================================================\n\n");
        String tableLine = "+------------+---------------------+----------------+-------------------------+\n";
        sb.append("Timeslots:\n");
        sb.append(tableLine);
        sb.append(String.format("| %-10s | %-19s | %-14s | %-10s | %-10s |\n",
                "Day", "Time", "CourseCode", "ActivityId", "Type"));
        sb.append(tableLine);

        List<TimeSlot> sortedSlots = timeSlots.stream()
                .filter(TimeSlot::isChosen)
                .sorted(Comparator.comparing(TimeSlot::getDay)
                        .thenComparing(TimeSlot::getStartTime))
                .collect(Collectors.toList());

        for (TimeSlot ts : sortedSlots) {
            sb.append(ts.toString()).append("\n");
        }
        sb.append(tableLine);
        return sb.toString();
    }
}
