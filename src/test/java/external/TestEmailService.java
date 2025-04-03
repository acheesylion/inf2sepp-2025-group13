package external;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestEmailService {
    private MockEmailService emailService;

    private static final int STATUS_SUCCESS = 0;
    private static final int STATUS_INVALID_SENDER_EMAIL = 1;
    private static final int STATUS_INVALID_RECIPIENT_EMAIL = -1;

    @BeforeEach
    void setUp(){
        emailService = new MockEmailService();
    }

    @Test
    void testSendEmailWithValidInputs(){
        int status = emailService.sendEmail("trini@gmail.com","boris_johnson@gmail.com",
                "Test Subject","Test Content");
        assertEquals(STATUS_SUCCESS, status);
    }

    @Test
    void testSendEmailWithInvalidSender(){
        int status = emailService.sendEmail("invalid-email", "boris_johnson@gmail.com",
                "Test Subject","Test Content");
        assertEquals(STATUS_INVALID_SENDER_EMAIL,status);

    }

    @Test
    void testSendEmailWithNullSender(){
        int status = emailService.sendEmail(null,"boris_johnson@gmail.com",
                "Test Subject", "Test Content");
        assertEquals(STATUS_INVALID_SENDER_EMAIL, status);
    }

    @Test
    void testSendEmailWithInvalidRecipient() {
        int status = emailService.sendEmail("trini@gmail.com", "invalid-email", "Test Subject", "Test Content");
        assertEquals(STATUS_INVALID_RECIPIENT_EMAIL, status);
    }

    @Test
    void testSendEmailWithNullRecipient() {
        int status = emailService.sendEmail("trini@gmail.com", null, "Test Subject", "Test Content");
        assertEquals(STATUS_INVALID_RECIPIENT_EMAIL, status);
    }

}
