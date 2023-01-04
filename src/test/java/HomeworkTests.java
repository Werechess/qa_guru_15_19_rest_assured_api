import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

class HomeworkTests {

    @BeforeAll
    static void setUp() {
        baseURI = "https://reqres.in";
    }

    @Test
    void checkUserExistsWithDelay() {
        given()
                .log().uri()
                .when()
                .get("/api/users?delay=3")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.email", hasItem("george.bluth@reqres.in"));
    }

    @Test
    void checkUserWhenCreated() {
        String data = "{ \"name\": \"morpheus\",\"job\": \"leader\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"),
                        "job", is("leader"),
                        "id", is(notNullValue()));
    }

    @Test
    void checkUserWhenUpdatedWithPut() {
        String data = "{ \"name\": \"morpheus\",\"job\": \"zion resident\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .put("/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", is("zion resident"));
    }

    @Test
    void checkUserWhenUpdatedWithPatch() {
        String data = "{ \"name\": \"morpheus\",\"job\": \"zion resident\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .patch("/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", is("zion resident"));
    }

    @Test
    void checkUserWhenDeleted() {
        given()
                .log().uri()
                .when()
                .delete("/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204)
                .body(is(emptyOrNullString()));
    }
}
