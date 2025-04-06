package model;

public class FAQItem {
    private final String question;
    private final String answer;
    private final String courseTag; // 可选的课程标签
    private final int number; // 唯一编号
    private static int nextNumber = 1; // 用于生成唯一编号

    public FAQItem(String question, String answer) {
        this(question, answer, null);
    }

    public FAQItem(String question, String answer, String courseTag) {
        this.question = question;
        this.answer = answer;
        this.courseTag = courseTag;
        this.number = nextNumber++;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
    
    public String getCourseTag() {
        return courseTag;
    }
    
    public boolean hasCourseTag() {
        return courseTag != null && !courseTag.isEmpty();
    }
    
    public int getNumber() {
        return number;
    }
}
