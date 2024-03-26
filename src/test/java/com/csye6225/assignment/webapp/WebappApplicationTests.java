package com.csye6225.assignment.webapp;


import com.csye6225.assignment.webapp.entity.User;
import com.csye6225.assignment.webapp.entity.UserEmail;
import com.csye6225.assignment.webapp.exception.ResourceNotFoundException;
import com.csye6225.assignment.webapp.repository.UserMailRepository;
import com.csye6225.assignment.webapp.repository.UserRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebappApplicationTests {
    @BeforeAll
    public static void setUp() {
        WebappApplication.main(new String[]{});
    }

    @Autowired
    private UserMailRepository userMailRepository;
    @Autowired
    private UserRepository userRepo;
    @AfterAll
    public void deleteUser() {
        User user = userRepo.findByEmail("kartikvtla@gmail.com")
                .orElseThrow(() -> new ResourceNotFoundException("Email", "Email Id", "kartikvtla@gmail.com"));
        userRepo.delete(user);
    }

    @Test
    public void getHealthcheck() {
        given()
                .when()
                .get("http://localhost:8080/healthz")
                .then()
                .statusCode(200);
    }

    @Test
    public void getMethodNotSupported() {

        given()
                .when()
                .post("http://localhost:8080/healthz")
                .then()
                .statusCode(405);
    }

    @Test
    public void getHealthcheckEndpointNotfound() {
        given()
                .when()
                .get("http://localhost:8080/healthz/kar")
                .then()
                .statusCode(404);
    }

    @Test
    public void getHealthcheckBadRequest() {
        String requestBody = "{\n" +
                "    \"last_name\": \"TestLastNameVanabhojana\"\n" +
                "}";
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .get("http://localhost:8080/healthz")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(1)
    public void adduser() {
        String requestBody = "{\n" +
                "    \"username\": \"kartikvtla@gmail.com\",\n" +
                "    \"password\": \"Test\",\n" +
                "    \"first_name\": \"TestFirstName\",\n" +
                "    \"last_name\": \"TestLastName\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("http://localhost:8080/v1/user")
                .then()
                .statusCode(201);
    }

    @Test
    public void existingEmailAdduser() {
        String requestBody = "{\n" +
                "    \"username\": \"kartikvtla@gmail.com\",\n" +
                "    \"password\": \"Test1\",\n" +
                "    \"first_name\": \"TestFirstName1\",\n" +
                "    \"last_name\": \"TestLastName1\"\n" +
                "}";


        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("http://localhost:8080/v1/user")
                .then()
                .statusCode(400);
    }

    @Test
    public void addUsermethodNotSupported() {
        String requestBody = "{\n" +
                "    \"username\": \"kartikvtla@gmail.com\",\n" +
                "    \"password\": \"Test1\",\n" +
                "    \"first_name\": \"TestFirstName1\",\n" +
                "    \"last_name\": \"TestLastName1\"\n" +
                "}";
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .get("http://localhost:8080/v1/user")
                .then()
                .statusCode(405);
    }


    @Test
    public void addUserbadrequestSupported() {
        String requestBody = "{\n" +
                "    \"username\": \"kartikvtla@gmail.com\",\n" +
                "    \"first_name\": \"TestFirstName1\",\n" +
                "    \"last_name\": \"TestLastName1\"\n" +
                "}";


        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("http://localhost:8080/v1/user")
                .then()
                .statusCode(400);
    }

    @Test
    public void testPostcheckfieldEmpty() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:8080/v1/user")
                .then()
                .statusCode(400);
    }

    @Test
    public void testPostEndpointNotfound() {
        String requestBody = "{\n" +
                "    \"username\": \"kartikvtla@gmail.com\",\n" +
                "    \"password\": \"Test1\",\n" +
                "    \"first_name\": \"TestFirstName1\",\n" +
                "    \"last_name\": \"TestLastName1\"\n" +
                "}";


        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("http://localhost:8080/v1/user/si")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(2)
    public void testgetself() {
        String username = "kartikvtla@gmail.com";
        String password = "Test";
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
User user =this.userRepo.findByEmail(username).get();
        Optional<UserEmail> userEmailOptional = this.userMailRepository.findByUser(user);
        if (userEmailOptional.isPresent()) {
            UserEmail userEmail = userEmailOptional.get();
            userEmail.setMailVerified(true);
            this.userMailRepository.save(userEmail);
        }
        given()
                .header("Authorization", authorizationHeader)
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:8080/v1/user/self")
                .then()
                .assertThat()
                .statusCode(200)
                .body("username", equalTo("kartikvtla@gmail.com"))
                .body("first_name", equalTo("TestFirstName"))
                .body("last_name", equalTo("TestLastName"));

    }

    @Test
    public void testgetselfAuthenticationFailed() {
        String username = "kartikvtla@gmail.com";
        String password = "Test123";
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());


        given()
                .header("Authorization", authorizationHeader)
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:8080/v1/user/self")
                .then()
                .assertThat()
                .statusCode(401);


    }

    @Test
    public void testgetselfbadRequest() {
        String requestBody = "{\n" +
                "    \"username\": \"kartikvtla@gmail.com\",\n" +
                "    \"password\": \"Test1\",\n" +
                "    \"first_name\": \"TestFirstName1\",\n" +
                "    \"last_name\": \"TestLastName1\"\n" +
                "}";
        String username = "kartikvtla@gmail.com";
        String password = "Test";
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .header("Authorization", authorizationHeader)
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:8080/v1/user/self")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testselfUnsupportedmethod() {
        String username = "kartikvtla@gmail.com";
        String password = "Test";
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());


        given()
                .header("Authorization", authorizationHeader)
                .accept(ContentType.JSON)
                .when()
                .patch("http://localhost:8080/v1/user/self")
                .then()
                .assertThat()
                .statusCode(405);
    }

    @Test
    public void testgetEndpointNotfound() {
        String username = "kartikvtla@gmail.com";
        String password = "Test";
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        given()
                .header("Authorization", authorizationHeader)
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:8080/v1/user/self/sss")
                .then()
                .assertThat()
                .statusCode(404);

    }

    @Test
    @Order(3)
    public void testPut() {
        String username = "kartikvtla@gmail.com";
        String password = "Test";
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        String requestBody = "{\n" +
                "    \"password\": \"Test\",\n" +
                "    \"first_name\": \"UpdateFirstName\",\n" +
                "    \"last_name\": \"UpdateTestLastName\"\n" +
                "}";
        given()
                .header("Authorization", authorizationHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("http://localhost:8080/v1/user/self")
                .then()
                .assertThat()
                .statusCode(204);
    }
  @Test
    @Order(4)
    public void testgetselfafterPut() {
      String username = "kartikvtla@gmail.com";
      String password = "Test";
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());


        given()
                .header("Authorization", authorizationHeader)
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:8080/v1/user/self")
                .then()
                .assertThat()
                .statusCode(200)
                .body("username", equalTo("kartikvtla@gmail.com"))
                .body("first_name", equalTo("UpdateFirstName"))
                .body("last_name", equalTo("UpdateTestLastName"));


    }

    @Test
    public void testPutFaildAuthentication() {
        String username = "kartikvtla@gmail.com";
        String password = "Test123";
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        String requestBody = "{\n" +
                "    \"password\": \"Test\",\n" +
                "    \"first_name\": \"UpdateFirstName\",\n" +
                "    \"last_name\": \"UpdateTestLastName\"\n" +
                "}";
        given()
                .header("Authorization", authorizationHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("http://localhost:8080/v1/user/self")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test
    @Order(5)
    public void testPutEmailEntered() {
        String username = "kartikvtla@gmail.com";
        String password = "Test";
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        String requestBody = "{\n" +
                "    \"username\": \"kartikvtla@gmail.com\",\n" +
                "    \"password\": \"Test\",\n" +
                "    \"first_name\": \"UpdateFirstName\",\n" +
                "    \"last_name\": \"UpdateTestLastName\"\n" +
                "}";
        given()
                .header("Authorization", authorizationHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("http://localhost:8080/v1/user/self")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testPutcheckfieldEmpty() {
        String username = "kartikvtla@gmail.com";
        String password = "Test";
        String authorizationHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        String requestBody = "{\n" +
                "    \"password\": \"Test\",\n" +
                "    \"first_name\": \"\",\n" +
                "    \"last_name\": \"Test\"\n" +
                "}";
        given()
                .header("Authorization", authorizationHeader)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("http://localhost:8080/v1/user/self")
                .then()
                .assertThat()
                .statusCode(400);
    }
}
