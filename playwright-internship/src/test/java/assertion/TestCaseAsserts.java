package assertion;

import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import pages.TestCasePage;

public class TestCaseAsserts {

    private final SoftAssert softAssert;

    public TestCaseAsserts() { this.softAssert = new SoftAssert(); }

    //Positive
    public void validateCreationNewTestCase(TestCasePage testCasePage, String title, String desc, String expResult) {
        boolean isVisible = testCasePage.isTestCaseVisible(title, desc, expResult);

        softAssert.assertTrue(testCasePage.isNotificationVisible("created"),
                "Notification does not contain 'created'.");
        softAssert.assertTrue(isVisible,
                "New test case is not visible.");
        softAssert.assertAll();
    }

    public void validateDeletionTestCase(TestCasePage testCasePage, String title) {
        Locator deletedTestCaseCard = testCasePage.findTestCaseCardByTitle(title, false);

        softAssert.assertTrue(testCasePage.isNotificationVisible("removed"),
                "Notification does not contain 'removed'.");
        softAssert.assertNull(deletedTestCaseCard,
                "Test case card is not null.");
        softAssert.assertAll();
    }

    public void validateUpdateTestCaseValidData(TestCasePage testCasePage, String title, String desc, String expResult) {
        boolean isVisible = testCasePage.isTestCaseVisible(title, desc, expResult);

        softAssert.assertTrue(testCasePage.isNotificationVisible("updated"),
                "Notification does not contain 'updated'.");
        softAssert.assertTrue(isVisible,
                "Updated test case is not visible.");
        softAssert.assertAll();
    }

    public void validateShowTestCasePreview(TestCasePage testCasePage, String title) {
        boolean isVisible = testCasePage.isPreviewVisible(title);

        softAssert.assertTrue(isVisible,
                "Preview for test case '" + title + "' was not displayed correctly.");
        softAssert.assertAll();
    }

    public void validateClickBackButton(TestCasePage testCasePage) {
        boolean isOnDashboard = testCasePage.isOnDashboard();

        Assert.assertTrue(isOnDashboard, "Clicking the back arrow did not navigate to the Dashboard page.");
    }

    //Negative
    public void validateUpdateTestCaseInvalidData(TestCasePage testCasePage) {
        boolean hasValidationMsg = testCasePage.isValidationMessageVisible("Test case name already exist");

        softAssert.assertTrue(testCasePage.isNotificationVisible("failed"),
                "Notification does not contain 'failed'.");
        softAssert.assertTrue(hasValidationMsg,
                "Expected validation message 'Test case name already exist' was not shown.");
        softAssert.assertAll();
    }

    public void validateCreateWithoutExpectedResult(TestCasePage testCasePage) {
        boolean hasValidationMsg = testCasePage.isValidationMessageVisible("Expected result is required");

        softAssert.assertTrue(hasValidationMsg,
                "Expected validation message 'Expected result is required' was not shown.");
        softAssert.assertAll();
    }
}
