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
            for (Activity activity : activities) {
                if (activity.hasId(activityId)){
                    if (activity instanceof Lecture) {
                        return !(((Lecture) activity).getRecorded());
                    }
                }
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






}
