package assertion;

import org.testng.asserts.SoftAssert;
import pages.LoginPage;

public class LoginPageAsserts {

    private final SoftAssert softAssert;

    public LoginPageAsserts() {
        this.softAssert = new SoftAssert();
    }

    public void validateLogin(LoginPage loginPage) {

        softAssert.assertTrue(loginPage.isDashboardVisible(), "Dashboard should be visible after login");
        softAssert.assertTrue(loginPage.getCurrentUrl().contains("/dashboard"), "URL should contain dashboard");
        softAssert.assertAll();
    }
}
