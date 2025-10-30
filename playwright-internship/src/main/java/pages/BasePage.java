package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import java.nio.file.Paths;

public abstract class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected void click(String selector) {
        page.click(selector);
    }

    protected void fill(String selector, String text) {
        page.fill(selector, text);
    }

    protected void safeClick(String selector) {
        try {
            page.waitForSelector(selector);
            page.click(selector);
        } catch (PlaywrightException e) {
            takeScreenshot("click_failure_" + System.currentTimeMillis());
            throw new RuntimeException("Failed to click: " + selector, e);
        }
    }

    protected void safeNavigate(String url) {
        try {
            page.navigate(url);
            page.waitForURL(actualUrl -> actualUrl.contains("sandbox"));
        } catch (PlaywrightException e) {
            takeScreenshot("click_failure_" + System.currentTimeMillis());
            throw new RuntimeException("Failed to navigate to: " + url, e);
        }
    }

    protected void takeScreenshot(String name) {
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(Paths.get("screenshots/" + name + ".png")));
    }
}