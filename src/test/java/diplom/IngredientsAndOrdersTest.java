package diplom;

import com.Stellar;
import com.Client;
import com.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class IngredientsAndOrdersTest {

    Client client = new Client();
    User user = new User();
    Stellar ordersHelper = new Stellar();

    @Before
    public void setUp(){
        user.createRandomUser();
        ordersHelper.setIngredientsList();
    }

    @Test //тест покрывает случай заказа с ингредиентом
    public void orderCreationAuthorized(){
        client.userRegistration();
        client.setAccessToken();
        ordersHelper.createOrderAuthorized(client.getAccessToken());
        ordersHelper.getOrderResponse().then().assertThat()
                .body("success", equalTo(true))
                .and()
                .body("name", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void orderCreationUnauthorized(){
        client.userRegistration();
        ordersHelper.createOrderUnauthorized();
        ordersHelper.getOrderResponse().then().assertThat()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    public void orderCreationWithoutIngredient(){
        client.userRegistration();
        client.setAccessToken();
        ordersHelper.createOrderWithoutIngredient(client.getAccessToken());
        ordersHelper.getOrderResponse().then().assertThat()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Test
    public void orderCreationWithInvalidIngredientHash(){
        client.userRegistration();
        client.setAccessToken();
        ordersHelper.createOrderWithIncorrectIngredientHash(client.getAccessToken());
        ordersHelper.getOrderResponse().then().assertThat().statusCode(500);
    }

    @Test
    public void getOrdersByAuthorizedUser(){
        client.userRegistration();
        client.setAccessToken();
        ordersHelper.createOrderAuthorized(client.getAccessToken());
        ordersHelper.getOrderListWithAccessToken(client.getAccessToken());
        ordersHelper.getOrderResponse().then().assertThat()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void getOrdersByUnauthorizedUser(){
        ordersHelper.getOrderListWithoutAccessToken();
        ordersHelper.getOrderResponse().then().assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
}
