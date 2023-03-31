package pt.ua.tqsenv.integration.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchPage {
    private final WebDriver driver;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "search")
    private WebElement searchField;

    public void searchFor(String query) {
        searchField.sendKeys(query);
        searchField.sendKeys(Keys.ENTER);
    }
}
