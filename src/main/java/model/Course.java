package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The Course class represents a course in the system, containing information about
 * the course's activities, members, and other related details.
 */
public class Course {

    private final List<Activity> activities;
    private final List<String> members;
    private final String courseCode;
    private final String name;
    private final String description;
    private final boolean requiresComputers;
    private final String courseOrganiserName;
    private final String courseOrganiserEmail;
    private final String courseSecretaryName;
    private final String courseSecretaryEmail;
    private final int requiredTutorials;
    private final int requiredLabs;

    /**
     * Constructs a Course object with the specified details.
     *
     * @param courseCode          The unique code of the course.
     * @param name                The name of the course.
     * @param description         A brief description of the course.
     * @param requiresComputers   Whether the course requires computers.
     * @param courseOrganiserName The name of the course organiser.
     * @param courseOrganiserEmail The email of the course organiser.
     * @param courseSecretaryName The name of the course secretary.
     * @param courseSecretaryEmail The email of the course secretary.
     * @param requiredTutorials   The number of required tutorials for the course.
     * @param requiredLabs        The number of required labs for the course.
     */
    public Course(String courseCode, String name, String description, boolean requiresComputers,
                  String courseOrganiserName, String courseOrganiserEmail,
                  String courseSecretaryName, String courseSecretaryEmail,
                  int requiredTutorials, int requiredLabs) {
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.requiresComputers = requiresComputers;
        this.courseOrganiserName = courseOrganiserName;
        this.courseOrganiserEmail = courseOrganiserEmail;
        this.courseSecretaryName = courseSecretaryName;
        this.courseSecretaryEmail = courseSecretaryEmail;
        this.requiredTutorials = requiredTutorials;
        this.requiredLabs = requiredLabs;
        this.activities = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    /**
     * Adds an activity (lecture) to the course.
     *
     * @param activityId   The unique ID of the activity.
     * @param startDate    The start date of the activity.
     * @param startTime    The start time of the activity.
     * @param endDate      The end date of the activity.
     * @param endTime      The end time of the activity.
     * @param location     The location where the activity takes place.
     * @param day          The day of the week the activity occurs.
     * @param isRecorded   Whether the activity is recorded.
     * @param type         The type of activity, should be "lecture".
     */
    public void addActivity(int activityId, LocalDate startDate, LocalTime startTime, LocalDate endDate,
                            LocalTime endTime, String location, DayOfWeek day, boolean isRecorded, String type) {



        activities.add(new Lecture(activityId, startDate, startTime, endDate, endTime, location, day, isRecorded));

    }

    /**
     * Adds an activity (tutorial or lab) to the course.
     *
     * @param activityId   The unique ID of the activity.
     * @param startDate    The start date of the activity.
     * @param startTime    The start time of the activity.
     * @param endDate      The end date of the activity.
     * @param endTime      The end time of the activity.
     * @param location     The location where the activity takes place.
     * @param day          The day of the week the activity occurs.
     * @param capacity     The capacity of the tutorial or lab.
     * @param type         The type of activity, should be either "lab" or "tutorial".
     */
     public void addActivity(int activityId, LocalDate startDate, LocalTime startTime, LocalDate endDate,
                        LocalTime endTime, String location, DayOfWeek day, int capacity, String type) {

         if (Objects.equals(type, "lab")){activities.add(new Lab(activityId, startDate, startTime, endDate, endTime, location, day, capacity));}
         if (Objects.equals(type, "tutorial")){activities.add(new Tutorial(activityId, startDate, startTime, endDate, endTime, location, day, capacity));}

    }

    /**
     * Retrieves an activity by its unique ID.
     *
     * @param id The ID of the activity.
     * @return The activity with the specified ID, or null if not found.
     */
    private Activity getActivity(int id) {
        for (Activity activity : activities) {
            if (activity.hasId(id)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * Adds a member to the course.
     *
     * @param member The email or identifier of the member to add.
     */
    public void addMember(String member){
        members.add(member);
    }

    /**
     * Retrieves a list of all members enrolled in the course.
     *
     * @return A list of member identifiers.
     */
    public List<String> getMembers(){
        return members;
    }

    /**
     * Removes all activities from the course.
     */
    public void removeActivities() {
        activities.clear();
    }

    /**
     * Checks if the course code matches the given code.
     *
     * @param code The code to check.
     * @return true if the course code matches, false otherwise.
     */
    public boolean hasCode(String code) {
        return this.courseCode.equals(code);
    }

    /**
     * Retrieves a list of all activities associated with the course.
     *
     * @return A list of activities.
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * Checks if the specified lecture is unrecorded.
     *
     * @param activityId The ID of the activity to check.
     * @return true if the activity is a lecture and it is unrecorded, false otherwise.
     */
    public boolean isUnrecordedLecture(int activityId) {
        Activity activity = getActivity(activityId);
            if (activity instanceof Lecture) {
                return !(((Lecture) activity).getRecorded());
            }
        return false;
    }

    /**
     * Gets the course code.
     *
     * @return The course code.
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Gets the course name.
     *
     * @return The course name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the email of the course organiser.
     *
     * @return The email of the course organiser.
     */
    public String getCourseOrganiserEmail() {
        return courseOrganiserEmail;
    }

    /**
     * Gets the number of required tutorials for the course.
     *
     * @return The number of required tutorials.
     */
    public int getRequiredTutorials() {
        return requiredTutorials;
    }

    /**
     * Gets the number of required labs for the course.
     *
     * @return The number of required labs.
     */
    public int getRequiredLabs() {
        return requiredLabs;
    }

    /**
     * Retrieves the day of the week for a specific activity.
     *
     * @param activityId The ID of the activity.
     * @return The day of the week for the activity, or null if not found.
     */
    public DayOfWeek getActivityDay(int activityId) {
        Activity activity = getActivity(activityId);
        if (activity == null) {
            return null;
        }
        return activity.getDay();
    }

    /**
     * Retrieves the start time for a specific activity.
     *
     * @param activityId The ID of the activity.
     * @return The start time of the activity, or null if not found.
     */
    public LocalTime getActivityStartTime(int activityId) {
        Activity activity = getActivity(activityId);
        if (activity == null) {
            return null;
        }
        return activity.getStartTime();
    }

    /**
     * Retrieves the end time for a specific activity.
     *
     * @param activityId The ID of the activity.
     * @return The end time of the activity, or null if not found.
     */
    public LocalTime getActivityEndTime(int activityId) {
        Activity activity = getActivity(activityId);
        if (activity == null) {
            return null;
        }
        return activity.getEndTime();
    }

    /**
     * Represents the Course object as a formatted string, including its details and activities.
     *
     * @return A string representation of the course and its activities.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("========================================================================\n");
        sb.append(String.format("Course: %s - %s\n", courseCode, name));
        sb.append(String.format("Description: %s\n", description));
        sb.append(String.format("Organiser: %s <%s>\n", courseOrganiserName, courseOrganiserEmail));
        sb.append(String.format("Secretary: %s <%s>\n", courseSecretaryName, courseSecretaryEmail));
        sb.append("========================================================================\n\n");

        String tableLine = "+------------+---------------------+----------------+---------------------------+\n";
        sb.append("Activities:\n");
        sb.append(tableLine);
        sb.append(String.format("| %-10s | %-19s | %-14s | %-25s |\n",
                "Day", "Time", "ActivityId", "Type"));
        sb.append(tableLine);

        List<Activity> sortedActivities = activities.stream()
                .sorted(Comparator.comparing(Activity::getDay)
                        .thenComparing(Activity::getStartTime))
                .collect(Collectors.toList());

        // Print each activity
        for (Activity activity : sortedActivities) {
            sb.append(activity.toString()).append("\n");
        }
        sb.append(tableLine);

        return sb.toString();
    }

}
