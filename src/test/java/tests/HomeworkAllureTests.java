package tests;

import api.AuthorizationApi;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import models.lombok.TestCaseModel;
import models.lombok.TestCaseNameModel;
import models.lombok.TestStepModel;
import models.lombok.TestStepsModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.ArrayList;
import java.util.List;

import static api.AllureTestCaseApi.createTestCase;
import static api.AllureTestCaseApi.createTestSteps;
import static api.AuthorizationApi.ALLURE_TESTOPS_SESSION;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static helpers.CustomApiListener.withCustomTemplates;

class HomeworkAllureTests {

    @BeforeAll
    static void beforeAll() {
        Configuration.baseUrl = "https://allure.autotests.cloud";
        RestAssured.baseURI = "https://allure.autotests.cloud";
        RestAssured.filters(withCustomTemplates());
    }

    @Test
    @Tag("homework")
    void createTestStepWithApiTest() {
        AuthorizationApi authorizationApi = new AuthorizationApi();
        String xsrfToken = authorizationApi.getXsrfToken();
        String authorizationCookie = authorizationApi.getAuthorizationCookie();

        String projectId = "1722";
        Faker faker = new Faker();
        TestCaseNameModel testCaseName = new TestCaseNameModel();
        testCaseName.setName(faker.name().nameWithMiddle());
        TestCaseModel testCase = createTestCase(xsrfToken, authorizationCookie, testCaseName, projectId);

        TestStepsModel steps = new TestStepsModel();
        List<TestStepModel> testCaseSteps = new ArrayList<>();
        List<String> stepNames = new ArrayList<>();
        int numberOfSteps = faker.number().numberBetween(1, 5);
        for (int i = 0; i < numberOfSteps; i++) {
            TestStepModel step = new TestStepModel();
            String name = faker.name().firstName();
            step.setName(name);
            stepNames.add(name);
            testCaseSteps.add(step);
        }
        steps.setSteps(testCaseSteps);
        createTestSteps(xsrfToken, authorizationCookie, steps, testCase.getId());

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie(ALLURE_TESTOPS_SESSION, authorizationCookie));

        open("/project/" + projectId + "/test-cases/" + testCase.getId());
        $$(".TestCaseScenarioStep__name").shouldHave(texts(stepNames));
    }
}
