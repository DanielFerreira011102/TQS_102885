package com.example.project;

import com.example.project.pages.ConfirmationPage;
import com.example.project.pages.HomePage;
import com.example.project.pages.PurchasePage;
import com.example.project.pages.ReservePage;
import io.github.bonigarcia.seljup.DockerBrowser;
import io.github.bonigarcia.seljup.EnabledIfDockerAvailable;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Dimension;;
import org.openqa.selenium.WebDriver;

import static io.github.bonigarcia.seljup.BrowserType.CHROME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Note that to run this test, you need to have Docker installed and running on your machine.
 * Selenium-Jupiter will then automatically discover the latest version of the browser in Docker Hub,
 * pull the image if necessary, and start the browser in a Docker container.
 */
@EnabledIfDockerAvailable
@ExtendWith(SeleniumJupiter.class)
public class BlazerunnerTest {

    private WebDriver driver;
    private HomePage homePage;
    private ReservePage reservePage;
    private PurchasePage purchasePage;
    private ConfirmationPage confirmationPage;


    @BeforeEach
    public void initializeDriver(@DockerBrowser(type = CHROME, version = "beta") WebDriver driver) {
        this.driver = driver;
        driver.get("https://blazedemo.com/");
        driver.manage().window().setSize(new Dimension(1042, 802));
        homePage = new HomePage(driver);
        reservePage = new ReservePage(driver);
        purchasePage = new PurchasePage(driver);
        confirmationPage = new ConfirmationPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void blazerunner() {
        homePage.selectDepartureCity("Boston");
        homePage.selectDestinationCity("Berlin");
        homePage.clickFindFlights();

        reservePage.clickChooseThisFlight();

        purchasePage.fillOutPersonalInformation("Johnny", "123 Street", "SomeTown", "New state", "9876");
        purchasePage.fillOutPaymentInformation("4", "2022", "John Michael");

        purchasePage.clickRememberMe();
        purchasePage.clickPurchaseFlight();

        assertThat(confirmationPage.getPrice()).isEqualTo("555 USD");
        assertThat(confirmationPage.getPageTitle()).isEqualTo("BlazeDemo Confirmation");
    }
}
