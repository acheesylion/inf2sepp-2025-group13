package system_tests;

import controller.AdminStaffController;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.SharedContext;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import view.TextUserInterface;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


public class AddCourseSystemTest extends TUITest {
    @Test
    public void testBaseAddCourse() throws URISyntaxException, IOException, ParseException {
        setMockInput(
                "0",
                "MATH12345",
                "Math For Computer Science",
                "fun course",
                "Sarah Smith",
                "sarah_smith@gmail.com",
                "Joash Lemmings",
                "joash_smith@gmail.com",
                "2",
                "1",
                "Y",
                "2",
                "3",
                "4"
        );
        View view = new TextUserInterface();
        SharedContext context = new SharedContext(view);
        AdminStaffController adminStaffController = new AdminStaffController(context, new TextUserInterface(), new MockAuthenticationService(), new MockEmailService());
        startOutputCapture();
        adminStaffController.manageCourses();
    }


}