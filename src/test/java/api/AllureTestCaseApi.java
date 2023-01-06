package api;

import models.lombok.TestCaseModel;
import models.lombok.TestCaseNameModel;
import models.lombok.TestStepsModel;

import static api.AuthorizationApi.ALLURE_TESTOPS_SESSION;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;

public class AllureTestCaseApi {

    public static TestCaseModel createTestCase(String xsrfToken, String authorizationCookie, TestCaseNameModel testCaseNameModel, String projectId) {
        return given()
                .header("X-XSRF-TOKEN", xsrfToken)
                .cookies("XSRF-TOKEN", xsrfToken,
                        ALLURE_TESTOPS_SESSION, authorizationCookie)
                .body(testCaseNameModel)
                .contentType(JSON)
                .log().all()
                .queryParam("projectId", projectId)
                .when()
                .post("/api/rs/testcasetree/leaf")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is(testCaseNameModel.getName()))
                .extract().as(TestCaseModel.class);
    }

    public static void createTestSteps(String xsrfToken, String authorizationCookie, TestStepsModel steps, Integer caseId) {
        given()
                .header("X-XSRF-TOKEN", xsrfToken)
                .cookies("XSRF-TOKEN", xsrfToken,
                        ALLURE_TESTOPS_SESSION, authorizationCookie)
                .body(steps)
                .contentType(JSON)
                .log().all()
                .when()
                .post("/api/rs/testcase/{caseId}/scenario", caseId)
                .then()
                .log().all()
                .statusCode(200);
    }
}
