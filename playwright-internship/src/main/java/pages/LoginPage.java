package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;

import static utils.ConfigReader.getValidEmail;
import static utils.ConfigReader.getValidPassword;
import static utils.Timeouts.DEFAULT_TIMEOUT;

public class LoginPage extends BasePage {
    //Selectors
    private final String LOGIN_BUTTON_SELECTOR = "a[href='/login']";
    private final String USERNAME_SELECTOR = "input[name='email']";
    private final String PASSWORD_SELECTOR = "input[name='password']";
    private final String SUBMIT_BUTTON_SELECTOR = "button:has-text('Login')";
    private final String ERROR_MESSAGE_SELECTOR = "label#validation-msg";
    private final String DASHBOARD_SELECTOR = "a:has-text('Logout')";

    //Locators
    private final Locator initialLoginButton = page.getByRole(AriaRole.LINK,
            new Page.GetByRoleOptions().setName("Login")).
            and(page.locator(".landing-buttons--btn"));
    private final Locator usernameInput = page.getByPlaceholder("Email");
    private final Locator passwordInput =  page.getByPlaceholder("Password");
    private final Locator loginButton = page.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Login"));

    public LoginPage(Page page) {
        super(page);
    }

    public void navigateToLogin(String url) {
        try {
            safeNavigate(url);
            initialLoginButton.click();
        } catch (TimeoutError e) {
            throw new RuntimeException("Failed to navigate to login page: " + e.getMessage());
        }
    }

    public void loginExpectSuccess(String username, String password) {
        try {
            fillLoginFormWithLocators(username, password);
            page.waitForSelector(DASHBOARD_SELECTOR, new Page.WaitForSelectorOptions().setTimeout(DEFAULT_TIMEOUT));
        } catch (Exception e) {
            Assert.fail("Login success flow failed: " + e.getMessage());
        }
    }

    //Selector approach
    private void fillLoginForm(String username, String password) {
        try {
            page.waitForSelector(USERNAME_SELECTOR);
            fill(USERNAME_SELECTOR, username);
            fill(PASSWORD_SELECTOR, password);
            safeClick(SUBMIT_BUTTON_SELECTOR);
        } catch (Exception e) {
            Assert.fail("Login success flow failed: " + e.getMessage());
        }
    }

    //Locator approach
    private void fillLoginFormWithLocators(String username, String password) {
        try {
            usernameInput.click();
            usernameInput.fill(username);
            passwordInput.click();
            passwordInput.fill(password);
            loginButton.click();
        }
        catch (Exception e) {
            Assert.fail("Login form flow failed: " + e.getMessage());
            takeScreenshot("Login form flow failed: " + e.getMessage());
        }
    }

    public boolean isDashboardVisible() {
        try {
            return page.isVisible(DASHBOARD_SELECTOR);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorVisible() {
        try {
            return page.isVisible(ERROR_MESSAGE_SELECTOR);
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return page.textContent(ERROR_MESSAGE_SELECTOR);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getCurrentUrl() {
        return page.url();
    }
}