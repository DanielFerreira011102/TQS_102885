package pt.ua.tqsenv.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pt.ua.tqsenv.AirQualityApplication;
import pt.ua.tqsenv.integration.pages.CachePage;
import pt.ua.tqsenv.integration.pages.ErrorPage;
import pt.ua.tqsenv.integration.pages.ResultsPage;
import pt.ua.tqsenv.integration.pages.SearchPage;

import java.time.Duration;

class F_FunctionalWeb_IT {
  private ChromeDriver driver;
  private SearchPage searchPage;
  private ResultsPage resultsPage;
  private CachePage cachePage;
  private ErrorPage errorPage;

  private static ConfigurableApplicationContext app;

  @BeforeAll
  public static void startSpringBootApplication() {
    app = SpringApplication.run(AirQualityApplication.class);
  }

  @AfterAll
  public static void stopSpringBootApplication() {
    app.stop();
  }

  @BeforeEach
  public void initializeDriver() {
    // I think the newest Chrome version is not 100% compatible
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

  @AfterEach
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  void FunctionalWeb() {

    assertThat(driver.getTitle()).isEqualTo("Air Quality Test App");

    searchPage.searchFor("Viseu");

    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // Wait for 10 seconds

    resultsPage.clickDefra();
    resultsPage.clickEpa();
    resultsPage.clickCacheLink();

    assertThat(cachePage.getRequestCount()).isEqualTo("1");
    assertThat(cachePage.getCacheHits()).isEqualTo("0");
    assertThat(cachePage.getCacheMisses()).isEqualTo("1");
    assertThat(cachePage.getExpiredCount()).isEqualTo("0");
    cachePage.clickSearchLink();

    searchPage.searchFor("Lisbon");

    resultsPage.clickSearchLink();

    searchPage.searchFor("Viseu");

    assertThat(resultsPage.getCityText()).isEqualTo("VISEU, PT");
    resultsPage.assertEpaIsPresent();
    resultsPage.clickCacheLink();

    assertThat(cachePage.getRequestCount()).isEqualTo("3");
    assertThat(cachePage.getCacheHits()).isEqualTo("1");
    assertThat(cachePage.getCacheMisses()).isEqualTo("2");
    assertThat(cachePage.getExpiredCount()).isEqualTo("0");
    cachePage.clickSearchLink();

    searchPage.searchFor("fffffffffffffffff");

    assertThat(errorPage.getErrorMessage()).isEqualTo("ERROR 404: Not Found");
    errorPage.clickCacheLink();

    assertThat(cachePage.getRequestCount()).isEqualTo("4");
    assertThat(cachePage.getCacheHits()).isEqualTo("1");
    assertThat(cachePage.getCacheMisses()).isEqualTo("3");
    assertThat(cachePage.getExpiredCount()).isEqualTo("0");
  }
}
