package model;

public class CourseInfo {
    private String courseCode;
    private String name;
    private String description;
    private Boolean requiresComputers;  // This can be a boolean, but keeping it as String for now
    private String courseOrganiserName;
    private String courseOrganiserEmail;
    private String courseSecretaryName;
    private String courseSecretaryEmail;
    private String requiredTutorials;  // This can be an integer, but keeping it as String for now
    private String requiredLabs;  // This can be an integer, but keeping it as String for now


    // Constructor to initialize all private fields
    public CourseInfo(){}

    public void setField(String fieldName, String item) {
        switch (fieldName) {
            case "courseCode":
                this.courseCode = item;
                break;
            case "name":
                this.name = item;
                break;
            case "description":
                this.description = item;
                break;
            case "courseOrganiserName":
                this.courseOrganiserName = item;
                break;
            case "courseOrganiserEmail":
                this.courseOrganiserEmail = item;
                break;
            case "courseSecretaryName":
                this.courseSecretaryName = item;
                break;
            case "courseSecretaryEmail":
                this.courseSecretaryEmail = item;
                break;
            case "requiredTutorials":
                this.requiredTutorials = item;
                break;
            case "requiredLabs":
                this.requiredLabs = item;
                break;
        }
    }
    public void setRequiresComputers(Boolean requiresComputers) {
        this.requiresComputers = requiresComputers;
    }

    public String getCourseInfo() {
        return String.format(
                "Course Code: %s\nName: %s\nDescription: %s\nRequires Computers: %s\n" +
                        "Course Organiser: %s (%s)\nCourse Secretary: %s (%s)\n" +
                        "Required Tutorials: %s\nRequired Labs: %s",
                courseCode, name, description, requiresComputers,
                courseOrganiserName, courseOrganiserEmail,
                courseSecretaryName, courseSecretaryEmail,
                requiredTutorials, requiredLabs
        );
    }
    public String getCourseCode() {return courseCode;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public Boolean getRequiresComputers() {return requiresComputers;}
    public String getCourseOrganiserName() {return courseOrganiserName;}
    public String getCourseOrganiserEmail() {return courseOrganiserEmail;}
    public String getCourseSecretaryName() {return courseSecretaryName;}
    public String getCourseSecretaryEmail() {return courseSecretaryEmail;}
    public String getRequiredTutorials() {return requiredTutorials;}
    public String getRequiredLabs() {return requiredLabs;}

}
