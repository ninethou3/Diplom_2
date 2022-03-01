package diplom;

import com.Client;
import com.User;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginTests {

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
    @DisplayName("Тест на возможность корректной авторизации")
    public void userLoginIsPossible(){
        client.userRegistration();
        client.userLogin();
        client.getResponse().then().assertThat()
                .body("success", equalTo(true))
                .and().statusCode(200);
    }

    @Test //незарегистрированный юзер не может залогиниться = система возвращает ошибку если указать неверный логин и пароль
    @DisplayName("Тест на логин с невалидными данными пользователя")
    public void loginWithIncorrectCredentialsNotAvailable(){
        //пропускаем шаг создания(регистрации)
        client.userLogin();
        client.getResponse().then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and().statusCode(401);
    }
}
