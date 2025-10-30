package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;

import static utils.Timeouts.SHORT_TIMEOUT;

public class TestCasePage extends BasePage {

    private Locator testCaseCard;
    private final Locator newTestCaseLink = page.getByRole(AriaRole.LINK,
            new Page.GetByRoleOptions().setName("New Test Case"));
    private final Locator titleInput =  page.getByPlaceholder("Title");
    private final Locator descInput =  page.getByPlaceholder("Description");
    private final Locator expResultInput =  page.getByPlaceholder("Expected Result");
    private final Locator testStepsInput =  page.getByPlaceholder("Test step");
    private final Locator addTestStepButton = page.getByText("Add Test Step");
    private final Locator automatedSwitch = page.locator("div.react-switch-bg");
    private final Locator submitButton = page.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Submit"));

    public TestCasePage(Page page) {
        super(page);
    }

    public Locator getTestCaseCard() {
        return testCaseCard;
    }

    public void setTestCaseCard(Locator testCaseCard) {
        this.testCaseCard = testCaseCard;
    }

    public void navigateToTestCases(String url) {
        try {
            safeNavigate(url);
        } catch (TimeoutError e) {
            throw new RuntimeException("Failed to navigate to test cases page: " + e.getMessage());
        }
    }

    public void createNewTestCaseExpectSuccess(String title, String desc, String expResult, String testStep) {
        try {
            fillNewTestCaseForm(title, desc, expResult, testStep);
        } catch (Exception e) {
            Assert.fail("Creating new test case success flow failed: " + e.getMessage());
        }
    }

    private void fillNewTestCaseForm(String title, String desc, String expResult, String testStep) {
        try {
            newTestCaseLink.click();

            titleInput.click();
            titleInput.fill(title);

            descInput.click();
            descInput.fill(desc);

            expResultInput.click();
            expResultInput.fill(expResult);

            testStepsInput.click();
            testStepsInput.fill(testStep);

            submitButton.click();
        } catch (Exception e) {
            Assert.fail("New test case form flow failed: " + e.getMessage());
            takeScreenshot("New test case form flow failed: " + e.getMessage());
        }
    }

    public void deleteTestCaseExpectSuccess(String title) {
        try {
            deleteTestCase(title);
        } catch (Exception e) {
            Assert.fail("Deleting test case success flow failed: " + e.getMessage());
        }
    }

    private void deleteTestCase(String title) {
        try {
            testCaseCard = findTestCaseCardByTitle(title, true);
            testCaseCard.click();
            Locator deleteButton = page.locator("button.btn.btn-danger");
            deleteButton.click();

            // Wait for the modal overlay to appear
            Locator confirmDialog = page.locator("div.confirmation-dialog");
            confirmDialog.waitFor(new Locator.WaitForOptions().setTimeout(SHORT_TIMEOUT));

            // Find and click the red "Remove" button inside the dialog
            Locator removeButton = page.locator("div.confirmation-dialog--buttons--confirm");
            removeButton.waitFor(new Locator.WaitForOptions().setTimeout(SHORT_TIMEOUT));
            removeButton.click();

        } catch (Exception e) {
            Assert.fail("Deleting test case flow failed: " + e.getMessage());
            takeScreenshot("Deleting test case flow failed: " + e.getMessage());
        }
    }

    public void updateTestCaseExpectSuccess(
            String oldTitle, String newTitle, String newDesc, String newResult,
            String newTestStep1, String newTestStep2, boolean isAutomated) {
        try {
            updateTestCase(oldTitle, newTitle, newDesc, newResult,
                    newTestStep1, newTestStep2, isAutomated);
        } catch (Exception e) {
            takeScreenshot("Update test case failed: " + e.getMessage());
            throw e;
        }
    }

    private void updateTestCase(
            String oldTitle, String newTitle, String newDesc, String newResult,
            String newTestStep1, String newTestStep2, boolean isAutomated) {
        try {
            testCaseCard = findTestCaseCardByTitle(oldTitle, true);
            testCaseCard.click();

            titleInput.click();
            titleInput.clear();
            titleInput.fill(newTitle);

            descInput.click();
            descInput.clear();
            descInput.fill(newDesc);

            expResultInput.click();
            expResultInput.clear();
            expResultInput.fill(newResult);

            addTestStepButton.click();

            testStepsInput.nth(0).click();
            testStepsInput.nth(0).clear();
            testStepsInput.nth(0).fill(newTestStep1);

            testStepsInput.nth(1).click();
            testStepsInput.nth(1).fill(newTestStep2);

            String style = automatedSwitch.getAttribute("style");
            boolean isCurrentlyOn = style != null && style.contains("37, 183, 232"); // blue = ON

            if (isAutomated && !isCurrentlyOn) {
                automatedSwitch.click();
            } else if (!isAutomated && isCurrentlyOn) {
                automatedSwitch.click();
            }

            submitButton.click();
        } catch (Exception e) {
            Assert.fail("Updating test case flow failed: " + e.getMessage());
            takeScreenshot("Updating test case flow failed: " + e.getMessage());
        }
    }

    public void showPreviewExpectedSuccess(String title) {
        try {
            showPreview(title);
        } catch (Exception e) {
            Assert.fail("Showing test case preview success flow failed: " + e.getMessage());
        }
    }

    private void showPreview(String title) {
        try {
            testCaseCard = findTestCaseCardByTitle(title, true);

            Locator previewButton = testCaseCard.getByText("Preview");
            previewButton.click();

        } catch (Exception e) {
            Assert.fail("Showing test case preview flow failed: " + e.getMessage());
            takeScreenshot("Showing test case preview flow failed: " + e.getMessage());
        }
    }

    public void clickNavigateBackButton() {
        Locator navigateBackButton = page.locator("a.navigate-left");
        navigateBackButton.click();
    }

    public boolean isOnDashboard() {
        page.waitForURL("**/dashboard", new Page.WaitForURLOptions().setTimeout(5000));

        return page.url().contains("/dashboard");
    }

    public void createTestCaseWithoutExpectedResult(String title, String desc, String testStep) {
        try {
            fillNewTestCaseForm(title, desc, "", testStep);
        } catch (Exception e) {
            Assert.fail("Creating test case without expected result failed: " + e.getMessage());
        }
    }

    public boolean isValidationMessageVisible(String expectedMessage) {
        try {
            Locator validationMsg = page.locator("#validation-msg");
            validationMsg.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            String text = validationMsg.textContent().trim();
            return text.equalsIgnoreCase(expectedMessage);
        } catch (Exception e) {
            System.out.println("Validation message not found: " + e.getMessage());
            return false;
        }
    }

    public boolean isNotificationVisible(String expectedMessage) {
        try {
            Locator toast = page.locator("div.Toastify__toast-body");
            toast.waitFor(new Locator.WaitForOptions().setTimeout(5000));

            String toastText = toast.textContent().trim();

//            System.out.println("Notification text: " + toastText);

            return toastText.toLowerCase().contains(expectedMessage.toLowerCase());
        } catch (Exception e) {
            System.out.println("No notification found or timeout: " + e.getMessage());
            return false;
        }
    }

    public Locator findTestCaseCardByTitle(String title, boolean shouldExist) {
        Locator testCasesGrid = page.locator("div.portrait-grid");
        testCasesGrid.waitFor(new Locator.WaitForOptions().setTimeout(SHORT_TIMEOUT));

        String selector = "a.preview-card:has-text('" + title + "')";

        if (shouldExist) {
            // Wait for it to appear (used in create/update validation)
            page.waitForSelector(selector,
                    new Page.WaitForSelectorOptions().setTimeout(SHORT_TIMEOUT));
        } else {
            // Wait for it to disappear (used in delete validation)
            page.waitForSelector(selector,
                    new Page.WaitForSelectorOptions()
                            .setTimeout(SHORT_TIMEOUT)
                            .setState(WaitForSelectorState.DETACHED));
            return null;
        }

        Locator testCaseCard = testCasesGrid.locator(selector).first();
        testCaseCard.scrollIntoViewIfNeeded();

        if (!testCaseCard.isVisible()) {
            throw new RuntimeException("Test case card not visible even after scroll: " + title);
        }

        return testCaseCard;
    }

    public boolean isTestCaseVisible(String title, String desc, String expResult) {
        try {
            testCaseCard = findTestCaseCardByTitle(title, true);

            boolean hasDescription = testCaseCard.locator("text=" + desc).isVisible();
            boolean hasExpected = testCaseCard.locator("text=" + expResult).isVisible();

            return hasDescription && hasExpected;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPreviewVisible(String title) {
        try {
            Locator previewDialog = page.locator("div.modal.fade.show");

            previewDialog.waitFor(new Locator.WaitForOptions().setTimeout(5000));

            boolean hasTitle = previewDialog.locator("text=" + title).isVisible();

            boolean hasCloseButton = previewDialog.getByRole(AriaRole.BUTTON,
                    new Locator.GetByRoleOptions().setName("Close")).isVisible();

            return previewDialog.isVisible() && hasTitle && hasCloseButton;
        } catch (Exception e) {
            System.out.println("Preview modal not visible or timed out: " + e.getMessage());
            return false;
        }
    }
}
