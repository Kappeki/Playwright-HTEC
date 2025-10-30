package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.TestCasePage;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static utils.ConfigReader.getBaseUrl;

public class TestBase {
    protected static Playwright playwright;
    protected static Browser browser;
    protected Page page;
    protected LoginPage loginPage;

    @BeforeSuite(alwaysRun = true)
    public static void setupClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

    }

    @AfterSuite(alwaysRun = true)
    public static void tearDownClass() {
        browser.close();
        playwright.close();
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        page = browser.newPage();
        page.context().tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)
            .setSources(true));
        loginPage = new LoginPage(page);
        loginPage.navigateToLogin(getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(org.testng.ITestResult result) {
        if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            String tracePath = "target/playwright-report/trace-" + System.currentTimeMillis() + ".zip";
            String screenShotPath = "screenshots/" + System.currentTimeMillis() + ".png";
            page.context().tracing().stop(new Tracing.StopOptions().setPath(Paths.get(tracePath)));
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenShotPath)));
            try (FileInputStream fis = new FileInputStream(screenShotPath)) {
                Allure.addAttachment("Screenshot", fis);
            } catch (IOException e) {
                System.err.println("Failed to attach screenshot: " + e.getMessage());
            }
        } else {
            page.context().tracing().stop();
        }
        page.close();
    }
}
