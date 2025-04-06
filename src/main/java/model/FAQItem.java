package model;

/**
 * The FAQItem class represents a single FAQ item consisting of a question, an answer,
 * and an optional course tag. Each FAQ item is assigned a unique number.
 */
public class FAQItem {
    private final String question;
    private final String answer;
    private final String courseTag; // Optional course tag
    private final int number; // Unique identifier
    private static int nextNumber = 1; // Used to generate unique numbers

    /**
     * Constructs a FAQItem with a question and an answer, without a course tag.
     *
     * @param question The question of the FAQ item.
     * @param answer   The answer to the FAQ item.
     */
    public FAQItem(String question, String answer) {
        this(question, answer, null);
    }

    /**
     * Constructs a FAQItem with a question, an answer, and an optional course tag.
     *
     * @param question The question of the FAQ item.
     * @param answer   The answer to the FAQ item.
     * @param courseTag The optional course tag for the FAQ item.
     */
    public FAQItem(String question, String answer, String courseTag) {
        this.question = question;
        this.answer = answer;
        this.courseTag = courseTag;
        this.number = nextNumber++;
    }

    /**
     * Gets the question of the FAQ item.
     *
     * @return The question of the FAQ item.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Gets the answer to the FAQ item.
     *
     * @return The answer to the FAQ item.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Gets the course tag associated with the FAQ item.
     *
     * @return The course tag, or null if no course tag is set.
     */
    public String getCourseTag() {
        return courseTag;
    }

    /**
     * Checks whether the FAQ item has an associated course tag.
     *
     * @return true if a course tag is set, false otherwise.
     */
    public boolean hasCourseTag() {
        return courseTag != null && !courseTag.isEmpty();
    }

    /**
     * Gets the unique number assigned to the FAQ item.
     *
     * @return The unique number for the FAQ item.
     */
    public int getNumber() {
        return number;
    }
}
