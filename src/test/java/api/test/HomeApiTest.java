package api.test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;

public class HomeApiTest {
    @Test
    public void secondPageUsersAvatars(){
        /*
         * given
         * //настройка
         * //что передавать
         *   when //когда завершили настройке, как и куда передавать
         *   then // что делаем после передачи
         * */
        Response users = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .body("page",notNullValue())
                .body("per_page",notNullValue())
                .body("data.id",not(hasItem((nullValue()))))
                .statusCode(200)
                .extract()
                .response();

        JsonPath users_page = users.jsonPath();

        Assert.assertFalse(users_page.get("page").toString().isEmpty(),"page is null");

        List <String> a = users_page.getList("data.avatar");
        for(String x : a){
            System.out.println(x);
            Assert.assertFalse(x.isEmpty(),"data.id is null");
        }
    }

    @Test
    public void loginTests(){
        String user1 = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\" }";
        String user2 = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"\" }";
        Response newUser1 = given()
                .contentType("application/json")
                .body(user1)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonNewUser1 = newUser1.jsonPath();
        System.out.println(jsonNewUser1.get("id").toString());
        Assert.assertFalse(jsonNewUser1.get("id").toString().isEmpty());

        Response newUser2 = given()
                .contentType("application/json")
                .body(user2)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .response();

        JsonPath jsonNewUser2 = newUser2.jsonPath();
        System.out.println(jsonNewUser2.get("error").toString());
        Assert.assertEquals(jsonNewUser2.get("error"), "Missing password");
    }
}
