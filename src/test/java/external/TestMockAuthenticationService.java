package external;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.*;

public class TestMockAuthenticationService {
    private static MockAuthenticationService authenticationService;
    //private static final String EXPECTED_ERROR = new JSONObject(){{
    //    put("error", "Wrong username or password");
    //}}.toJSONString();

    @BeforeAll
    @DisplayName("")
    static void setup() throws URISyntaxException, IOException, ParseException {
        authenticationService = new MockAuthenticationService();
    }

    @Test
    @DisplayName("")
    void testValidLogin() throws ParseException{
        String result = authenticationService.login("student1","student1pass");

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(result);

        assertEquals("student1",json.get("username"));
        assertEquals("student1pass", json.get("password"));

    }

    @Test
    @DisplayName("")
    void testLoginWithInvalidUser() throws ParseException{
        String result = authenticationService.login("invalidUser","password");

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(result);

        assertEquals("Wrong username or password", json.get("error"));

    }

    @Test
    @DisplayName("")
    void testLoginWithInvalidPassword() throws ParseException{
    String result = authenticationService.login("student1","notStudent1Pass");

    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(result);

    assertEquals("Wrong username or password", json.get("error"));

    }
}
