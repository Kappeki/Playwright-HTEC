package tests;

import assertion.LoginPageAsserts;
import base.TestBase;
import org.testng.annotations.Test;

import static utils.ConfigReader.getValidEmail;
import static utils.ConfigReader.getValidPassword;


public class LoginTests extends TestBase {

    @Test(groups = {"smoke", "UI"},
        description = "Verify successful login with valid credentials"
    )
    public void testValidLogin() {

        loginPage.loginExpectSuccess(getValidEmail(), getValidPassword());

        LoginPageAsserts loginPageAsserts = new LoginPageAsserts();
        loginPageAsserts.validateLogin(loginPage);

    }
}