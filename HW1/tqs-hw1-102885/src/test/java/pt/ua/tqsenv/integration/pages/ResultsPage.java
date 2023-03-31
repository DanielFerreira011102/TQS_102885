package pt.ua.tqsenv.integration.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultsPage {
    private final WebDriver driver;

    public ResultsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "defra")
    private WebElement defraButton;

    @FindBy(id = "epa")
    private WebElement epaButton;

    @FindBy(css = "li:nth-child(2) svg")
    private WebElement cacheLink;

    @FindBy(css = "li:nth-child(1) svg")
    private WebElement searchLink;

    @FindBy(id = "city")
    private WebElement cityElement;

    @FindBy(id = "num")
    private WebElement epaAQIElement;

    public void clickDefra() {
        defraButton.click();
    }

    public void clickEpa() {
        epaButton.click();
    }

    public void clickCacheLink() {
        cacheLink.click();
    }

    public void clickSearchLink() {
        searchLink.click();
    }

    public String getCityText() {
        return cityElement.getText();
    }

    public void assertEpaIsPresent() {
        assertThat(epaAQIElement).isNotNull();
    }
}
