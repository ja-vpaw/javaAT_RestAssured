package api.test;




import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FirstApiTest {

    @Test
    public void jsobArrayTest(){




    }

    @Test
    public void jsobObjectTest(){
        JSONObject jo = new JSONObject();
        JSONObject ja = new JSONObject();
        JSONObject ji = new JSONObject();

        ji.put("name","morpheus");
        ji.put("job","lr");
       /* ji.put("data",ja);
        ja.put("user",jo);
        jo.put("age","31");
*/
        System.out.println(ji);

        given()
                .contentType("application/json")
                .body(ji)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().all();
    }


    @Test
    public void mapmapTest(){
        Map<String, Object> dataGlobal = new HashMap<String, Object>();
        Map<String, Object> dataFirst = new HashMap<String, Object>();
        Map<String, String> dataSecond = new HashMap<String, String>();
        Object[] mas = new Object[3];

        dataSecond.put("age","31");
        mas[0]=dataSecond;
        dataSecond= new HashMap<String, String>();
        dataSecond.put("age","32");
        mas[1]=dataSecond;
        dataSecond= new HashMap<String, String>();
        dataSecond.put("age","33");
        mas[2]=dataSecond;

        dataFirst.put("data", mas);
        dataGlobal.put("name", "morpheus");
        dataGlobal.put("job", "leader");
        dataGlobal.put("user", dataFirst);

        given()
                .contentType("application/json")
                .when()
                .body(dataGlobal)
                .post("https://reqres.in/api/users")
                .then()
                .log().all();
    }

    @Test
    public void firstTest2(){


       Response resp = given()
                .when()
                .get("https://reqres.in/api/users")
                .then()
                .log().all()
        .extract()
        .response();

        JsonPath jsonNewUser = resp.jsonPath();

        Assert.assertFalse(jsonNewUser.get("page").toString().isEmpty(),"page is null");
        jsonNewUser.getList("data.id")
                .forEach(x->Assert.assertFalse(x.toString().isEmpty(),"data.id is null"));

        List <Integer> a = jsonNewUser.getList("data.id");

        for(Integer x : a){
            System.out.println(x);
            Assert.assertFalse(x.toString().isEmpty(),"data.id is null");
        }

        System.out.println(a);
    }

    @Test
    public void firstTest(){
        /*
        * given
        * //настройка
        * //что передавать
        *   when //когда завершили настройке, как и куда передавать
        *   then // что делаем после передачи
        * */

        given()
                .when()
                .get("https://reqres.in/api/users")
                .then()
                .log().all()
                .body("page",notNullValue())
                .body("per_page",notNullValue())
                .body("data.id",not(hasItem((nullValue()))));
    }

    @Test
    public void secondTest(){
        Map<String,String> data = new HashMap<String, String>();
        data.put("name","Kirill");
        data.put("job","teacher");
        Response newUser = given()
                .contentType("application/json")
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .response();

        JsonPath jsonNewUser = newUser.jsonPath();
        Assert.assertEquals(jsonNewUser.get("name"),data.get("name"));
        Assert.assertEquals(jsonNewUser.get("job"),data.get("job"));
    }
}
