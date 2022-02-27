package diplom;

import com.Base;
import com.Client;
import com.User;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class RegisterTests {

    Client client = new Client();
    User user = new User();

    @Before
    public void setUp(){
        user.createRandomUser();
    }

    @After
    public void delete(){
        client.delete();
    }

    @Test
    @DisplayName("Тест на возможность создания пользователя")
    public void userCreationIsPossible(){
        client.userRegistration();
        client.getResponse()
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Тест на невозможность создать пользователя с теми же данными")
    public void duplicateUserTest(){
        client.userRegistration();
        client.userRegistration();
        client.getResponse().then().assertThat().body("success", equalTo(false)).and().statusCode(403);
    }

    @Test
    @DisplayName("Тест на попытку создать пользователя без передачи поля почты")
    public void userCreationWithoutEmailNotPossible (){
        String createRequestBody = "{\"password\":\"" + user.getPassword() + "\","
                + "\"name\":\"" + user.getName() + "\"}";

        client.setResponse(given()
                .spec(Base.getBaseSpec())
                .body(createRequestBody)
                .when()
                .post("auth/register"));
        client.getResponse()
                .then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }

    @Test
    @DisplayName("Тест на попытку создать пользователя без передачи поля пароля")
    public void userCreationWithoutPasswordNotPossible (){
        String createRequestBody = "{\"email\":\"" + user.getEmail() + "\","
                + "\"name\":\"" + user.getName() + "\"}";
        client.setResponse(given()
                .spec(Base.getBaseSpec())
                .body(createRequestBody)
                .when()
                .post("auth/register"));
        client.getResponse()
                .then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }

    @Test
    @DisplayName("Тест на попытку создать пользователя без передачи поля имени")
    public void userCreationWithoutNameNotPossible (){
        String createRequestBody = "{\"email\":\"" + user.getEmail() + "\","
                + "\"password\":\"" + user.getPassword() + "\"}";
        client.setResponse(given()
                .spec(Base.getBaseSpec())
                .body(createRequestBody)
                .when()
                .post("auth/register"));
        client.getResponse()
                .then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }
}
