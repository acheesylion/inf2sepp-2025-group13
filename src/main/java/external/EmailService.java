package external;

/**
 * API for interacting with an Email Service Provider
 */
public interface EmailService {
    int STATUS_SUCCESS = 0;
    int STATUS_INVALID_SENDER_EMAIL = 1;
    int STATUS_INVALID_RECIPIENT_EMAIL = 2;
    int STATUS_UNKNOWN_ERROR = 100;

    /**
     *
     * @param sender email address of the sender
     * @param recipient email address of the recipient
     * @param subject subject line for the email
     * @param content content of the email in plaintext
     * @return status code provided by the Email Service Provider
     */
    int sendEmail(String sender, String recipient, String subject, String content);

    class TestMockEmailService {
        //@BeforeEach prepare test environment
        //@AfterEach clean up test environment
        //@BeforeAll: executed before all tests in the test class to e.g.
        //connect to a database
        //@AfterAll: executed after all tests to e.g. disconnect from a
        //database
        //@BeforeEach
        void assertEquals(String expectedResult, MockEmailService testEmail) {

        }
        //test result for if sender == null
        //test result for if recipient == null
        //test result for

       // @Test
        //@DisplayName("First Test")
        //void shouldShowSimpleAssertion() {
        //    Assertions.assertEquals(1,2);
       // }
    }
}
