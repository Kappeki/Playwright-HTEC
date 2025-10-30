package tests;

import assertion.TestCaseAsserts;
import base.TestBase;
import io.qameta.allure.Allure;
import jdk.jfr.Description;
import org.testng.annotations.Test;
import pages.TestCasePage;
import utils.AllureLogger;

import static utils.ConfigReader.*;

public class TestCaseTests extends TestBase {

    private TestCasePage testCasePage;

    //Positive
    @Test(groups = {"smoke", "regression"},
            description = "Verify successful creation of new test case with valid data",
            priority = 1
    )
    public void testValidNewTestCase() {
        String title = "DusanTest";
        String desc = "This is description";
        String expResult = "Success";
        String stepOne = "Click button";
        testCasePage = new TestCasePage(page);

        AllureLogger.logStep("Login with valid credentials");
        loginPage.loginExpectSuccess(getValidEmail(), getValidPassword());

        AllureLogger.logStep("Navigate to Test Cases page");
        testCasePage.navigateToTestCases(getBaseUrl() + "testcases");

        AllureLogger.logStep("Filling form and submitting new test case");
        testCasePage.createNewTestCaseExpectSuccess(title, desc, expResult, stepOne);

        AllureLogger.logStep("Verifying that the created test case is visible and correctly saved");
        TestCaseAsserts testCaseAsserts = new TestCaseAsserts();
        testCaseAsserts.validateCreationNewTestCase(testCasePage, title, desc, expResult);
    }

    @Test(groups = {"regression"},
            description = "Verify successful deletion of an already existing test case",
            priority = 4,
            dependsOnMethods = "testValidNewTestCase"
    )
    public void testValidDeleteTestCase() {
        String title = "DusanNewTest";
        testCasePage = new TestCasePage(page);

        AllureLogger.logStep("Login with valid credentials");
        loginPage.loginExpectSuccess(getValidEmail(), getValidPassword());

        AllureLogger.logStep("Navigate to Test Cases page");
        testCasePage.navigateToTestCases(getBaseUrl() + "testcases");

        AllureLogger.logStep("Deleting existing test case: " + title);
        testCasePage.deleteTestCaseExpectSuccess(title);

        AllureLogger.logStep("Validating that the test case is deleted and no longer visible");
        TestCaseAsserts testCaseAsserts = new TestCaseAsserts();
        testCaseAsserts.validateDeletionTestCase(testCasePage, title);
    }

    @Test(groups = {"regression"},
            description = "Verify successful update of a test case with valid data",
            priority = 3,
            dependsOnMethods = "testValidNewTestCase"
    )
    public void testValidUpdateTestCase() {
        String oldTitle = "DusanTest";
        String newTitle = "DusanNewTest";
        String newDesc = "Updated description by Dusan Petrovic";
        String newResult = "Updated result";
        String newTestStep1 = "New test step1";
        String newTestStep2 = "New test step2";
        boolean isAutomated = true;

        testCasePage = new TestCasePage(page);

        AllureLogger.logStep("Login with valid credentials");
        loginPage.loginExpectSuccess(getValidEmail(), getValidPassword());

        AllureLogger.logStep("Navigate to Test Cases page");
        testCasePage.navigateToTestCases(getBaseUrl() + "testcases");

        AllureLogger.logStep("Updating existing test case with new data");
        testCasePage.updateTestCaseExpectSuccess(
                oldTitle, newTitle, newDesc, newResult,
                newTestStep1, newTestStep2, isAutomated);

        AllureLogger.logStep("Validating that the update was successful and new data is displayed");
        TestCaseAsserts testCaseAsserts = new TestCaseAsserts();
        testCaseAsserts.validateUpdateTestCaseValidData(testCasePage, newTitle, newDesc, newResult);
    }

    @Test(groups = {"ui", "regression"},
            description = "Verify that preview is correctly shown",
            dependsOnMethods = "testValidNewTestCase"
    )
    public void testValidPreviewShow() {
        String title = "DusanTest";
        testCasePage = new TestCasePage(page);

        AllureLogger.logStep("Login with valid credentials");
        loginPage.loginExpectSuccess(getValidEmail(), getValidPassword());

        AllureLogger.logStep("Navigate to Test Cases page");
        testCasePage.navigateToTestCases(getBaseUrl() + "testcases");

        AllureLogger.logStep("Clicking preview button for test case: " + title);
        testCasePage.showPreviewExpectedSuccess(title);

        AllureLogger.logStep("Verifying that preview is displayed correctly");
        TestCaseAsserts testCaseAsserts = new TestCaseAsserts();
        testCaseAsserts.validateShowTestCasePreview(testCasePage, title);
    }

    @Test(groups = {"ui"},
            description = "Verify that clicking the back arrow on the Test Cases page correctly navigates to the Dashboard."
    )
    public void testNavigateBackButton() {
        testCasePage = new TestCasePage(page);

        AllureLogger.logStep("Login with valid credentials");
        loginPage.loginExpectSuccess(getValidEmail(), getValidPassword());

        AllureLogger.logStep("Navigate to Test Cases page");
        testCasePage.navigateToTestCases(getBaseUrl() + "testcases");

        AllureLogger.logStep("Clicking back button on Test Cases page");
        testCasePage.clickNavigateBackButton();

        AllureLogger.logStep("Verifying that navigation leads back to Dashboard");
        TestCaseAsserts testCaseAsserts = new TestCaseAsserts();
        testCaseAsserts.validateClickBackButton(testCasePage);
    }

    //Negative
    @Test(groups = {"negative"},
            description = "Verify that updating a test case with an already existing title shows validation error and fails to save.",
            priority = 2,
            dependsOnMethods = "testValidNewTestCase"
    )
    public void testInvalidUpdateWithDuplicateTitle() {
        testCasePage = new TestCasePage(page);

        AllureLogger.logStep("Login with valid credentials");
        loginPage.loginExpectSuccess(getValidEmail(), getValidPassword());

        AllureLogger.logStep("Navigate to Test Cases page");
        testCasePage.navigateToTestCases(getBaseUrl() + "testcases");

        AllureLogger.logStep("Attempting to update test case with duplicate title");
        testCasePage.updateTestCaseExpectSuccess(
                "DusanTest",
                "DusanExistingTest",
                "Updated description by Dusan Petrovic",
                "Updated result",
                "New test step1",
                "New test step2",
                true
        );

        AllureLogger.logStep("Verifying that validation error appears and data is not saved");
        TestCaseAsserts testCaseAsserts = new TestCaseAsserts();
        testCaseAsserts.validateUpdateTestCaseInvalidData(testCasePage);
    }

    @Test(groups = {"negative"},
            description = "Verify that creating a test case without entering Expected Result shows validation error and fails to submit."
    )
    public void testCreateTestCaseWithoutExpectedResult() {
        testCasePage = new TestCasePage(page);

        AllureLogger.logStep("Login with valid credentials");
        loginPage.loginExpectSuccess(getValidEmail(), getValidPassword());

        AllureLogger.logStep("Navigate to Test Cases page");
        testCasePage.navigateToTestCases(getBaseUrl() + "testcases");

        AllureLogger.logStep("Attempting to create a new test case without expected result field");
        testCasePage.createTestCaseWithoutExpectedResult(
                "Missing Expected Result Test",
                "This description is fine but expected result is missing",
                "Click button"
        );

        AllureLogger.logStep("Verifying that validation error is displayed and form submission fails");
        TestCaseAsserts testCaseAsserts = new TestCaseAsserts();
        testCaseAsserts.validateCreateWithoutExpectedResult(testCasePage);
    }
}
