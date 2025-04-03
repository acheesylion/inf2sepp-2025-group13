package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Course {
        // Instance variables
        private final List<Activity> activities;
        private String courseCode;
        private String name;
        private String description;
        private boolean requiresComputers;
        private String courseOrganiserName;
        private String courseOrganiserEmail;
        private String courseSecretaryName;
        private String courseSecretaryEmail;
        private int requiredTutorials;
        private int requiredLabs;
        private final List<String> members;



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
        public void addActivity(LocalDate startDate, LocalTime startTime, LocalDate endDate,
                                LocalTime endTime, String location, DayOfWeek day, boolean isRecorded, String type) {

            int id = UUID.randomUUID().hashCode();

            activities.add(new Lecture(id, startDate, startTime, endDate, endTime, location, day, isRecorded));

        }

         public void addActivity(LocalDate startDate, LocalTime startTime, LocalDate endDate,
                            LocalTime endTime, String location, DayOfWeek day, int capacity, String type) {

             int id = UUID.randomUUID().hashCode();
             if (Objects.equals(type, "lab")){activities.add(new Lab(id, startDate, startTime, endDate, endTime, location, day, capacity));}
             if (Objects.equals(type, "tutorial")){activities.add(new Tutorial(id, startDate, startTime, endDate, endTime, location, day, capacity));}

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

        public boolean hasActivityWithId(int id) {
            // Implement functionality to check if the activity with the given id exists
            return false;
        }

        public List<String> getActivitiesAsString() {
            // Implement functionality to return activities as a String
            List<String> activityList = new ArrayList<>();
            for (Activity activity : activities) {
                activityList.add(activity == null ? "null" : activity.toString());
            }
            return activityList;
        }

        public List<Activity> getActivities() {
            return activities;
        }

        public boolean isUnrecordedLecture(int activityId) {
            for (Activity activity : activities) {
                if (activity.hasId(activityId)){
                    if (activity instanceof Lecture) {
                        return !(((Lecture) activity).getRecorded());
                    }
                }
            }
            return false;
        }

        // Overriding the toString method to represent the Course object as a string
        @Override
        public String toString() {
            return "Course Code: " + courseCode + "\n" +
                    "Name: " + name + "\n" +
                    "Description: " + description + "\n" +
                    "Requires Computers: " + requiresComputers + "\n" +
                    "Course Organiser: " + courseOrganiserName + " (" + courseOrganiserEmail + ")\n" +
                    "Course Secretary: " + courseSecretaryName + " (" + courseSecretaryEmail + ")\n" +
                    "Required Tutorials: " + requiredTutorials + "\n" +
                    "Required Labs: " + requiredLabs;
        }

        // Getters and setters for instance variables (if needed)
        public String getCourseCode() {
            return courseCode;
        }

        public void setCourseCode(String courseCode) {
            this.courseCode = courseCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isRequiresComputers() {
            return requiresComputers;
        }

        public void setRequiresComputers(boolean requiresComputers) {
            this.requiresComputers = requiresComputers;
        }

        public String getCourseOrganiserName() {
            return courseOrganiserName;
        }

        public void setCourseOrganiserName(String courseOrganiserName) {
            this.courseOrganiserName = courseOrganiserName;
        }

        public String getCourseOrganiserEmail() {
            return courseOrganiserEmail;
        }

        public void setCourseOrganiserEmail(String courseOrganiserEmail) {
            this.courseOrganiserEmail = courseOrganiserEmail;
        }

        public String getCourseSecretaryName() {
            return courseSecretaryName;
        }

        public void setCourseSecretaryName(String courseSecretaryName) {
            this.courseSecretaryName = courseSecretaryName;
        }

        public String getCourseSecretaryEmail() {
            return courseSecretaryEmail;
        }

        public void setCourseSecretaryEmail(String courseSecretaryEmail) {
            this.courseSecretaryEmail = courseSecretaryEmail;
        }

        public int getRequiredTutorials() {
            return requiredTutorials;
        }

        public void setRequiredTutorials(int requiredTutorials) {
            this.requiredTutorials = requiredTutorials;
        }

        public int getRequiredLabs() {
            return requiredLabs;
        }

        public void setRequiredLabs(int requiredLabs) {
            this.requiredLabs = requiredLabs;
        }

}
