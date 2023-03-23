package io.cucumber.skeleton;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverFactory {
    public static WebDriver createWebDriver(String webdriver) {
        return switch (webdriver) {
            case "firefox" -> new FirefoxDriver();
            case "chrome" -> new ChromeDriver();
            default -> throw new RuntimeException("Unsupported webdriver: " + webdriver);
        };
    }
}