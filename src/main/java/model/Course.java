package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Course {
        // Instance variables
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




    // Constructor
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

        // Methods to manage activities
        public void addActivity(int activityId, LocalDate startDate, LocalTime startTime, LocalDate endDate,
                                LocalTime endTime, String location, DayOfWeek day, boolean isRecorded, String type) {



            activities.add(new Lecture(activityId, startDate, startTime, endDate, endTime, location, day, isRecorded));

        }

         public void addActivity(int activityId, LocalDate startDate, LocalTime startTime, LocalDate endDate,
                            LocalTime endTime, String location, DayOfWeek day, int capacity, String type) {

             if (Objects.equals(type, "lab")){activities.add(new Lab(activityId, startDate, startTime, endDate, endTime, location, day, capacity));}
             if (Objects.equals(type, "tutorial")){activities.add(new Tutorial(activityId, startDate, startTime, endDate, endTime, location, day, capacity));}

        }

        private Activity getActivity(int id) {
            for (Activity activity : activities) {
                if (activity.getId() == id) {
                    return activity;
                }
            }
            return null;
        }

        public void addMember(String member){
            members.add(member);
        }

        public List<String> getMembers(){
            return(this.members);
        }

        public void removeActivities() {
            // Implement the functionality to remove all activities
        }

        public boolean hasCode(String code) {
            return this.courseCode.equals(code);
        }

        public boolean hasActivityId(int id) {
            for (Activity activity : activities) {
                if (activity.getId() == id) {
                    return true;
                }
            }
            return false;
        }


        public List<Activity> getActivities() {
            return activities;
        }

        public boolean isUnrecordedLecture(int activityId) {
            Activity activity = getActivity(activityId);
                if (activity instanceof Lecture) {
                    return !(((Lecture) activity).getRecorded());
                }
            return false;
        }

        public String getCourseCode() {
            return courseCode;
        }
        public String getName() {
            return name;
        }
        public String getDescription() {
            return description;
        }
        public boolean isRequiresComputers() {
            return requiresComputers;
        }
        public String getCourseOrganiserName() {
            return courseOrganiserName;
        }
        public String getCourseOrganiserEmail() {
            return courseOrganiserEmail;
        }
        public String getCourseSecretaryName() {
            return courseSecretaryName;
        }
        public String getCourseSecretaryEmail() {
            return courseSecretaryEmail;
        }
        public int getRequiredTutorials() {
            return requiredTutorials;
        }
        public int getRequiredLabs() {
            return requiredLabs;
        }


        public DayOfWeek getDayId(int activityId) {
            Activity activity = getActivity(activityId);
            if (activity == null) {
                return null;
            }
            return activity.getDay();
        }

        public LocalTime getStartTimeId(int activityId) {
            Activity activity = getActivity(activityId);
            if (activity == null) {
                return null;
            }
            return activity.getStartTime();
        }

        public LocalTime getEndTimeId(int activityId) {
            Activity activity = getActivity(activityId);
            if (activity == null) {
                return null;
            }
            return activity.getEndTime();
        }

        // Overriding the toString method to represent the Course object as a string
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            // Print Course Details Header
            sb.append("========================================================================\n");
            sb.append(String.format("Course: %s - %s\n", getCourseCode(), getName()));
            sb.append(String.format("Description: %s\n", getDescription()));
            sb.append(String.format("Organiser: %s <%s>\n", getCourseOrganiserName(), getCourseOrganiserEmail()));
            sb.append(String.format("Secretary: %s <%s>\n", getCourseSecretaryName(), getCourseSecretaryEmail()));
            sb.append("========================================================================\n\n");

            // Print Activities Header
            String tableLine = "+------------+---------------------+------------+----------------+---------------------------+\n";
            sb.append("Activities:\n");
            sb.append(tableLine);
            sb.append(String.format("| %-10s | %-19s | %-10s | %-14s | %-25s |\n",
                    "Day", "Time", "CourseCode", "ActivityId", "Type"));
            sb.append(tableLine);

            // Formatter for time output
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            // Sort activities by day and then start time.
            List<Activity> sortedActivities = activities.stream()
                    .sorted(Comparator.comparing(Activity::getDay)
                            .thenComparing(Activity::getStartTime))
                    .collect(Collectors.toList());

            // Print each activity
            for (Activity activity : sortedActivities) {
                String day = activity.getDay().toString();
                String time = activity.getStartTime().format(timeFormatter) + " - " + activity.getEndTime().format(timeFormatter);
                String courseCode = getCourseCode();
                int activityId = activity.getId();
                String type;
                if (activity instanceof Lecture) {
                    if (((Lecture) activity).getRecorded()) {
                        type = "Lecture (recorded)";
                    } else {
                        type = "Lecture (unrecorded)";
                    }

                } else if (activity instanceof Lab) {
                    type = "Lab (Capacity: " + ((Lab) activity).getCapacity() + ")";
                } else if (activity instanceof Tutorial) {
                    type = "Tutorial (Capacity: " + ((Tutorial) activity).getCapacity() + ")";
                } else {
                    type = "Unknown";
                }
                sb.append(String.format("| %-10s | %-19s | %-10s | %-14d | %-25s |\n",
                        day, time, courseCode, activityId, type));
            }
            sb.append(tableLine);

            return sb.toString();
        }






}
