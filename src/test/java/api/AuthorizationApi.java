package api;

import static config.UserProperties.*;
import static io.restassured.RestAssured.given;

public class AuthorizationApi {

    public final static String ALLURE_TESTOPS_SESSION = "ALLURE_TESTOPS_SESSION";

    public String getXsrfToken() {
        return given()
                .formParam("grant_type", "apitoken")
                .formParam("scope", "openid")
                .formParam("token", TOKEN)
                .when()
                .post("/api/uaa/oauth/token")
                .then()
                .statusCode(200)
                .extract()
                .path("jti");
    }

    public String getAuthorizationCookie() {
        String xsrfToken = getXsrfToken();

        return given()
                .header("X-XSRF-TOKEN", xsrfToken)
                .header("Cookie", "XSRF-TOKEN=" + xsrfToken)
                .formParam("username", USERNAME)
                .formParam("password", PASSWORD)
                .when()
                .post("/api/login/system")
                .then()
                .statusCode(200).extract().response()
                .getCookie(ALLURE_TESTOPS_SESSION);
    }
}
