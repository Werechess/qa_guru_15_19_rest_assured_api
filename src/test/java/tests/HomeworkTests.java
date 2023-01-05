package tests;

import models.lombok.RequestUserModel;
import models.lombok.ResponseCreateUserModel;
import models.lombok.ResponseUpdateUserModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static specs.RequestSpecs.jsonRequestSpec;
import static specs.RequestSpecs.requestSpec;
import static specs.ResponseSpecs.responseSpec;

class HomeworkTests {

    @Test
    void checkUserExistsWithDelay() {
        given(requestSpec)
                .when()
                .get("/api/users?delay=3")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("data.email", hasItem("george.bluth@reqres.in"));
    }

    @Test
    void checkUserWhenCreated() {
        RequestUserModel data = new RequestUserModel();
        data.setName("morpheus");
        data.setJob("leader");

        ResponseCreateUserModel response = given(jsonRequestSpec)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .spec(responseSpec)
                .statusCode(201)
                .extract().as(ResponseCreateUserModel.class);

        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("leader");
        assertThat(response.getId()).isNotEmpty();
        assertThat(response.getCreatedAt()).isNotEmpty();
    }

    @Test
    void checkUserWhenUpdatedWithPut() {
        RequestUserModel data = new RequestUserModel();
        data.setName("morpheus");
        data.setJob("zion resident");

        ResponseUpdateUserModel response = given(jsonRequestSpec)
                .body(data)
                .when()
                .put("/api/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(ResponseUpdateUserModel.class);

        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("zion resident");
        assertThat(response.getUpdatedAt()).isNotEmpty();
    }

    @Test
    void checkUserWhenUpdatedWithPatch() {
        RequestUserModel data = new RequestUserModel();
        data.setName("morpheus");
        data.setJob("zion resident");

        ResponseUpdateUserModel response = given(jsonRequestSpec)
                .body(data)
                .when()
                .patch("/api/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(ResponseUpdateUserModel.class);

        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("zion resident");
        assertThat(response.getUpdatedAt()).isNotEmpty();
    }

    @Test
    void checkUserWhenDeleted() {
        given(requestSpec)
                .when()
                .delete("/api/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(204)
                .body(is(emptyOrNullString()));
    }
}
