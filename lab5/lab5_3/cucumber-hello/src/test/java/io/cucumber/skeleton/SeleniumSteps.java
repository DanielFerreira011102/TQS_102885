package io.cucumber.skeleton;

import io.cucumber.java.After;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/*
 Sometimes it does not work because of cookie popups and elements become non-interactable.
 How do we dismiss those popups when they appear?
 */
@ExtendWith(SeleniumJupiter.class)
public class SeleniumSteps {

    private ChromeDriver driver;

    @Given("I am on the Google search page")
    public void I_visit_google() {

        // it was not working without these options... why?
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*","ignore-certificate-errors");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(chromeOptions);

        driver.get("https://www.google.com");
    }

    @When("I search for {string}")
    public void search_for(String query) {
        WebElement element = driver.findElement(By.name("q"));
        // Enter something to search for
        element.sendKeys(query);
        // Now submit the form. WebDriver will find the form for us from the element
        element.submit();
    }

    @Then("the page title should start with {string}")
    public void checkTitle(String titleStartsWith) {
        // Google's search is rendered dynamically with JavaScript
        // Wait for the page to load timeout after ten seconds
        new WebDriverWait(driver, Duration.of(3, ChronoUnit.SECONDS)).until((ExpectedCondition<Boolean>) d -> {
            assert d != null;
            return d.getTitle().toLowerCase().startsWith(titleStartsWith);
        });
    }

    @After()
    public void closeBrowser() {
        driver.quit();
    }
}