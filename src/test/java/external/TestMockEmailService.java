package external;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMockEmailService {
    private MockEmailService emailService;

    private static final int STATUS_SUCCESS = 0;
    private static final int STATUS_INVALID_SENDER_EMAIL = 1;
    private static final int STATUS_INVALID_RECIPIENT_EMAIL = 2;

    @BeforeEach
    @DisplayName("Setting up an instance of MockEmailService before each test.")
    void setUp(){
        emailService = new MockEmailService();
    }

    @Test
    @DisplayName("Testing sending an email with valid inputs.")
    void testSendEmailWithValidInputs(){
        int status = emailService.sendEmail("student1@hindeburg.ac.uk","teacher1@hindeburg.ac.uk",
                "Test Subject","Test Content");
        assertEquals(STATUS_SUCCESS, status);
    }

    @Test
    @DisplayName("Testing trying to send an email with an invalid sender email.")
    void testSendEmailWithInvalidSender(){
        int status = emailService.sendEmail("invalid-email", "teacher1@hindeburg.ac.uk",
                "Test Subject","Test Content");
        assertEquals(STATUS_INVALID_SENDER_EMAIL,status);

    }

    @Test
    @DisplayName("Testing trying to send an email with a null sender email.")
    void testSendEmailWithNullSender(){
        int status = emailService.sendEmail(null,"teacher1@hindeburg.ac.uk",
                "Test Subject", "Test Content");
        assertEquals(STATUS_INVALID_SENDER_EMAIL, status);
    }

    @Test
    @DisplayName("Testing trying to send an email with an invalid recipient email.")
    void testSendEmailWithInvalidRecipient() {
        int status = emailService.sendEmail("student1@hindeburg.ac.uk", "invalid-email",
                "Test Subject", "Test Content");
        assertEquals(STATUS_INVALID_RECIPIENT_EMAIL, status);
    }

    @Test
    @DisplayName("Testing trying to send an email with a null recipient email.")
    void testSendEmailWithNullRecipient() {
        int status = emailService.sendEmail("student1@hindeburg.ac.uk", null,
                "Test Subject", "Test Content");
        assertEquals(STATUS_INVALID_RECIPIENT_EMAIL, status);
    }

}
