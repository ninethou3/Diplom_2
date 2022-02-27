package diplom;

import com.User;
import com.Client;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserTests {

    Client client = new Client();
    User user = new User();


    @Before
    public void setUp(){
        user.createRandomUser();
    }

    @After
    public void tearDown(){
        client.delete();
    }

    @Test
    @DisplayName("Тест на возможность изменить данные пользователя с авторизацией")
    public void authorisedUserCanChangeData(){
        client.userRegistration();
        client.setAccessToken();
        user.createRandomUser(); //эмулируем новые данные пользователя
        client.refreshUserData(client.getAccessToken());
        client.getUserData();
        client.getResponse().then().assertThat()
                .body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Тест на невозможность изменить данные пользователя без авторизации")
    public void userCredentialsNotChangeableUnauthorized (){
        //шаг регистрации не нужен, поскольку незарегистрированный пользователь как раз не имеет АксесТокена
        client.refreshUserDataUnauthorised();
        client.getResponse().then().assertThat()
                .body("success", equalTo(false))
                .and().statusCode(401);
    }
}
