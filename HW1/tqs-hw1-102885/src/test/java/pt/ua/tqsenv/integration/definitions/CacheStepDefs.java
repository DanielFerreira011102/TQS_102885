package pt.ua.tqsenv.integration.definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pt.ua.tqsenv.integration.pages.CachePage;
import pt.ua.tqsenv.integration.pages.ErrorPage;
import pt.ua.tqsenv.integration.pages.ResultsPage;
import pt.ua.tqsenv.integration.pages.SearchPage;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheStepDefs {

    private WebDriver driver;
    private SearchPage searchPage;
    private ResultsPage resultsPage;
    private CachePage cachePage;
    private ErrorPage errorPage;

    @Given("I am on the Air Quality Test App")
    public void goToAirQualityTestApp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1936, 1056));
        searchPage = new SearchPage(driver);
        resultsPage = new ResultsPage(driver);
        cachePage = new CachePage(driver);
        errorPage = new ErrorPage(driver);
    }

    @When("I search for {string}")
    public void searchForCity(String city) {
        searchPage.searchFor(city);
    }

    @And("I wait for {int} seconds")
    public void waitForSeconds(int seconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    @And("I click on the Defra, EPA, and Cache links")
    public void clickLinks() {
        resultsPage.clickDefra();
        resultsPage.clickEpa();
        resultsPage.clickCacheLink();
    }

    @Then("the request count should be {string}")
    public void checkRequestCount(String count) {
        assertThat(count).isEqualTo(cachePage.getRequestCount());
    }

    @Then("the cache hits count should be {string}")
    public void checkCacheHitsCount(String count) {
        assertThat(count).isEqualTo(cachePage.getCacheHits());
    }

    @Then("the cache misses count should be {string}")
    public void checkCacheMissesCount(String count) {
        assertThat(count).isEqualTo(cachePage.getCacheMisses());
    }

    @Then("the expired count should be {string}")
    public void checkExpiredCount(String count) {
        assertThat(count).isEqualTo(cachePage.getExpiredCount());
    }

    @And("I click on the Search link")
    public void clickOnSearchLink() {
        cachePage.clickSearchLink();
    }

    @Then("the city text should be {string}")
    public void checkCityText(String text) {
        assertThat(text).isEqualTo(resultsPage.getCityText());
    }

    @And("EPA should be present")
    public void checkEPAIsPresent() {
        resultsPage.assertEpaIsPresent();
    }

    @And("I click on the Cache link")
    public void clickOnCacheLink() {
        resultsPage.clickCacheLink();
    }

    @Then("the error message should be {string}")
    public void checkErrorMessage(String errorMessage) {
        assertThat(errorMessage).isEqualTo(errorPage.getErrorMessage());
    }
}
