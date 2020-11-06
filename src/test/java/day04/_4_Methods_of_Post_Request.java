package day04;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.Spartan;

import java.io.File;
import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
//   POST with: 1) String; 2) External File; 3)Map; 4)


public class _4_Methods_of_Post_Request {

    @BeforeAll
    public static void init(){
        RestAssured.baseURI = "http://3.226.201.68" ;
        RestAssured.port = 8000 ;
        RestAssured.basePath = "/api" ;
    }

    @DisplayName("Post request with String")
    @Test
    public void testPostWithStringBody(){

        // lets try to get random names rather than same ADAM each time
        Faker faker = new Faker();
        String randomName = faker.name().firstName();
        System.out.println("randomName = " + randomName);
        // try to randomize gender and phone num on your own at home.
        String bodyString = "{\n" +
                "  \"name\"  : \"" + randomName+ "\",\n" +
                "  \"gender\": \"Female\",\n" +
                "  \"phone\": 6234567890\n" +
                "}";
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(bodyString).
        when()
                .post("/spartans").
        then()
                .log().all()
                .statusCode(201)
                .body("data.name", is( randomName ) ) ;
        ;


    }

    @DisplayName("Post with external File")
    @Test
    public void testPostWithExternalFile(){

        // create a file object that point to spartan.json you just added
        // so we can use this as body in post request
        File file1 = new File("spartan.json");

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(file1).
        when()
                .post("/spartans").
        then()
                .log().all()
                .statusCode(201)
                .body("data.name",is("From File") ) ;

    }

    @DisplayName("Post with Map object")
    @Test
    public void testPostWithMapAsBody(){

        // please add dependency jackson-databind

        // create a Map<String,Object> as hashMap
        // add name , gender , phone
        // Back at 3:00PM EST
        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("name","Vincent");
        bodyMap.put("gender","Male");
        bodyMap.put("phone",3476346789l);

        System.out.println("bodyMap = " + bodyMap);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(bodyMap). // jackson-data-bind turn your java map object to json here
        when()
                .post("/spartans").
        then()
                .log().all()
                .statusCode(201)
                .body("data.name", is("Vincent")  )
        ;


    }

    @DisplayName("Post request with POJO")
    @Test
    public void testPostBodyWithPojo() {
        Spartan sp1 = new Spartan("William", "Male",2024569513 ) ;
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(sp1).
          when()
                .post("/spartans").
          then()
                .log().all()
                .statusCode(201)
        .assertThat().body("data.name", is("William"))
        .assertThat().body("data.gender", is("Male"))
        .assertThat().body("data.phone", is(2024569513));
    }
}
