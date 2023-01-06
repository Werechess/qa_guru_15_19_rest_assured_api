package tests;

import api.AuthorizationApi;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import models.lombok.TestCaseNameModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static api.AuthorizationApi.ALLURE_TESTOPS_SESSION;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static config.UserProperties.*;
import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;

class AllureTestOpsTests {

// create USER_TOKEN in baseUrl/user/30

    @BeforeAll
    static void beforeAll() {
        Configuration.baseUrl = "https://allure.autotests.cloud";
        RestAssured.baseURI = "https://allure.autotests.cloud";
        RestAssured.filters(withCustomTemplates());
    }

    @Test
    void loginTest() {
        open("");

        $(byName("username")).setValue(USERNAME);
        $(byName("password")).setValue(PASSWORD).pressEnter();

        $("button[aria-label=\"User menu\"]").click();
        $(".Menu__item_info").shouldHave(text(USERNAME));
    }

    @Test
    void loginWithApiSimpleTest() {
        String xsrfToken = given()
                .formParam("grant_type", "apitoken")
                .formParam("scope", "openid")
                .formParam("token", TOKEN)
                .when()
                .post("/api/uaa/oauth/token")
                .then()
                .statusCode(200)
                .extract()
                .path("jti");

        String authorizationCookie = given()
                .header("X-XSRF-TOKEN", xsrfToken)
                .header("Cookie", "XSRF-TOKEN=" + xsrfToken)
                .formParam("username", USERNAME)
                .formParam("password", PASSWORD)
                .when()
                .post("/api/login/system")
                .then()
                .statusCode(200).extract().response()
                .getCookie(ALLURE_TESTOPS_SESSION);

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie(ALLURE_TESTOPS_SESSION, authorizationCookie));

        open("");
        $("button[aria-label=\"User menu\"]").click();
        $(".Menu__item_info").shouldHave(text(USERNAME));
    }

    @Test
    void loginWithApiTest() {
        String authorizationCookie = new AuthorizationApi().getAuthorizationCookie();

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie(ALLURE_TESTOPS_SESSION, authorizationCookie));

        open("");
        $("button[aria-label=\"User menu\"]").click();
        $(".Menu__item_info").shouldHave(text(USERNAME));
    }

    @Test
    void viewTestCaseWithApiTest() {
    /*
        1. Make GET request to /api/rs/testcase/13328/overview
        2. Check name is "View test case name"
    */
        String authorizationCookie = new AuthorizationApi().getAuthorizationCookie();

        given()
                .log().all()
                .cookie(ALLURE_TESTOPS_SESSION, authorizationCookie)
                .get("/api/rs/testcase/13328/overview")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("View test case name"));
    }

    @Test
    void viewTestCaseWithUiTest() {
    /*
        1. Open page /project/1722/test-cases/13328
        2. Check name is "View test case name"
    */
        String authorizationCookie = new AuthorizationApi().getAuthorizationCookie();

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie(ALLURE_TESTOPS_SESSION, authorizationCookie));

        open("/project/1722/test-cases/13328");
        $(".TestCaseLayout__name").shouldHave(text("View test case name"));
    }

    @Test
    void createTestCaseWithApiTest() {
    /*
        1. Make POST request to /api/rs/testcasetree/leaf?projectId=1722
           with body {"name":"Some random test"}
        2. Get test case {id} from response {"id":13330,"name":"Some random test","automated":false,"external":false,"createdDate":1669920611154,"statusName":"Draft","statusColor":"#abb8c3"}
        3. Open page /project/1722/test-cases/{id}
        4. Check name is "Some random test"
    */
        AuthorizationApi authorizationApi = new AuthorizationApi();

        String xsrfToken = authorizationApi.getXsrfToken();
        String authorizationCookie = authorizationApi.getAuthorizationCookie();

        Faker faker = new Faker();
        String testCaseName = faker.name().nameWithMiddle();

        TestCaseNameModel testCaseNameModel = new TestCaseNameModel();
        testCaseNameModel.setName(testCaseName);
//        String testCaseBody = "{\"name\":\"Some random test\"}";

        int testCaseId = given()
                .log().all()
                .header("X-XSRF-TOKEN", xsrfToken)
                .cookies("XSRF-TOKEN", xsrfToken,
                        ALLURE_TESTOPS_SESSION, authorizationCookie)
                .body(testCaseNameModel)
                .contentType(JSON)
                .queryParam("projectId", "1722")
                .post("/api/rs/testcasetree/leaf")
//                .post("/api/rs/testcasetree/leaf?projectId=1722")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", is(testCaseName))
                .body("automated", is(false))
                .body("external", is(false))
                .extract()
                .path("id");

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie(ALLURE_TESTOPS_SESSION, authorizationCookie));
        open("/project/1722/test-cases/" + testCaseId);
        $(".TestCaseLayout__name").shouldHave(text(testCaseName));
    }
}
