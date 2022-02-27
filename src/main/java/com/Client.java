package com;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.given;

public class Client {

    private String accessToken;
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    User userBody = new User();

    @Step("Сеттер для токена")
    public void setAccessToken(){
        accessToken = response.then().extract().path("accessToken");
    }

    @Step("Геттер для токена")
    public String getAccessToken(){
        return accessToken;
    }

    @Step("Метод регистрации пользователя")
    public void userRegistration(){
        response = given()
                .spec(Base.getBaseSpec())
                .log().all()
                .body(userBody.createRandomUser())
                .when()
                .post("auth/register");
    }

    @Step("Метод входа под логином и паролем пользователя")
    public void userLogin(){
        String json = "{\"email\":\"" + userBody.getEmail() + "\"," + "\"password\":\"" + userBody.getPassword() + "\"}";
        response = given()
                .spec(Base.getBaseSpec())
                .body(json)
                .when()
                .log().all()
                .post("auth/login");
    }

    @Step("Метод обновление данных авторизованного пользователя ")
    public void refreshUserData(String accessToken){

        String json = "{\"password\":\"" + "passsword" + "\"}";

        response = given()
                .spec(Base.getBaseSpec())
                .headers("Authorization", accessToken)
                .body(json)
                .when()
                .log().all()
                .patch("auth/user");
    }

    @Step("Метод обновления данных не авторизованного пользователя")
    public void refreshUserDataUnauthorised(){
//    String json = "{\"email\":\"" + email + "\"," + "\"password\":\"" + password + "\"," + "\"name\":\"" + name + "\"}";

        response = given()
                .spec(Base.getBaseSpec())
                .body(userBody.createRandomUser())
                .when()
                .patch("auth/user");
    }

    @Step("Метод получения данных пользователя")
    public void getUserData(){
        response = given()
                .spec(Base.getBaseSpec())
                .headers("Authorization", accessToken)
                .when()
                .get("auth/user");
    }

    public void delete() {
        if (getAccessToken() == null) {
            return;
        }
        given()
                .spec(Base.getBaseSpec())
                .headers("Authorization", accessToken)
                .when()
                .delete("auth/user")
                .then()
                .statusCode(202);

        System.out.println(getAccessToken());
    }
}