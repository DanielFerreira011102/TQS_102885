package io.cucumber.skeleton;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.assertj.core.api.Assertions.assertThat;

public class BlazerunnerStepDefinitions {

    private WebDriver driver;

    @Given("I am on the Blazedemo homepage")
    public void iAmOnBlazedemoHomepage() {

        // I think the newest Chrome version is not 100% compatible
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1042, 802));
        driver.get("https://blazedemo.com/");
    }

    @When("I select the departure city as {string} and arrival city as {string}")
    public void iSelectDepartureAndArrivalCities(String departure, String arrival) {
        driver.findElement(By.name("fromPort")).click();
        WebElement fromDropdown = driver.findElement(By.name("fromPort"));
        fromDropdown.findElement(By.xpath("//option[. = '" + departure + "']")).click();

        driver.findElement(By.name("toPort")).click();
        WebElement toDropdown = driver.findElement(By.name("toPort"));
        toDropdown.findElement(By.xpath("//option[. = '" + arrival + "']")).click();
    }

    @When("I click on the Find Flights button")
    public void iClickOnFindFlightsButton() {
        driver.findElement(By.cssSelector(".btn-primary")).click();
    }

    @When("I select the flight {int} from the list")
    public void iSelectFirstFlightFromList(Integer flightNumber) {
        driver.findElement(By.cssSelector("tr:nth-child(%d) .btn".formatted(flightNumber))).click();
    }

    @When("I fill in the passenger details")
    public void iFillInPassengerDetails() {
        driver.findElement(By.id("inputName")).click();
        driver.findElement(By.id("inputName")).sendKeys("Johnny");

        driver.findElement(By.id("address")).sendKeys("123 Street");

        driver.findElement(By.id("city")).sendKeys("SomeTown");

        driver.findElement(By.id("state")).sendKeys("New state");

        driver.findElement(By.id("zipCode")).sendKeys("9876");

        driver.findElement(By.id("creditCardNumber")).sendKeys("4");

        driver.findElement(By.id("creditCardYear")).sendKeys("2022");

        driver.findElement(By.id("nameOnCard")).sendKeys("John Michael");
    }

    @When("I click on the Remember Me button")
    public void iClickOnRememberMeButton() {
        driver.findElement(By.cssSelector(".checkbox")).click();
    }

    @When("I confirm my booking")
    public void iConfirmBooking() {
        driver.findElement(By.cssSelector(".btn-primary")).click();
    }

    @Then("I should see the confirmation page with the price {int} USD")
    public void iSeeConfirmationPageWithCorrectPrice(Integer price) {
        assertThat(driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(2)")).getText()).isEqualTo("%d USD".formatted(price));
    }

    @Then("I should be on the confirmation page")
    public void iAmOnConfirmationPage() {
        assertThat(driver.getTitle()).isEqualTo("BlazeDemo Confirmation");
    }

    @Then("I close the browser")
    public void iCloseBrowser() {
        driver.quit();
    }
}
