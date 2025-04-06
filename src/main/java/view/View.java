package view;

import model.FAQ;
import model.FAQSection;
import model.Inquiry;

public interface View {
    String getInput(String prompt);
    boolean getYesNoInput(String prompt);
    void displayInfo(String text);
    void displaySuccess(String text);
    void displayWarning(String text);
    void displayError(String text);
    void displayException(Exception e);
    void displayDivider();
    void displayFAQ(FAQ faq);
    void displayFAQSection(FAQSection section);
    // 新增方法：显示按课程代码筛选的FAQ部分
    void displayFilteredFAQSection(FAQSection section, String courseCode);
    void displayInquiry(Inquiry inquiry);
}
