package model;

import java.time.LocalDateTime;

/**
 * The Inquiry class represents a student's inquiry about a course or other related subjects.
 * It contains information such as the inquirer's email, the subject of the inquiry,
 * the content of the inquiry, and the course code (if any). Additionally, it keeps track of when
 * the inquiry was created and who it is assigned to.
 */
public class Inquiry {
    private final LocalDateTime createdAt;
    private final String inquirerEmail;
    private final String subject;
    private final String content;
    private String courseCode;
    private String assignedTo;

    /**
     * Constructs a new Inquiry object with the specified details.
     *
     * @param inquirerEmail The email of the person who made the inquiry.
     * @param subject       The subject of the inquiry.
     * @param content       The content or description of the inquiry.
     * @param courseCode    The course code associated with the inquiry (can be null).
     */
    public Inquiry(String inquirerEmail, String subject, String content, String courseCode) {
        this.courseCode = courseCode;
        this.createdAt = LocalDateTime.now();
        this.inquirerEmail = inquirerEmail;
        this.subject = subject;
        this.content = content;
    }

    /**
     * Gets the creation time of the inquiry.
     *
     * @return The LocalDateTime when the inquiry was created.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the email of the person who made the inquiry.
     *
     * @return The email address of the inquirer.
     */
    public String getInquirerEmail() {
        return inquirerEmail;
    }

    /**
     * Gets the subject of the inquiry.
     *
     * @return The subject of the inquiry.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Gets the content of the inquiry.
     *
     * @return The content or description of the inquiry.
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the email address of the person assigned to the inquiry.
     *
     * @return The assigned person's email or null if not assigned.
     */
    public String getAssignedTo() {
        return assignedTo;
    }

    /**
     * Gets the course code associated with the inquiry, or a default message if no course code is provided.
     *
     * @return The course code, or "No course specified" if the course code is null.
     */
    public String getCourseCode() {
        return courseCode != null ? courseCode : "No course specified";
    }

    /**
     * Sets the person to whom the inquiry is assigned.
     *
     * @param assignedTo The email address of the person assigned to handle the inquiry.
     */
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}
