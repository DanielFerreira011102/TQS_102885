package pt.ua.tqsenv.integration.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ErrorPage {
    private final WebDriver driver;

    public ErrorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "span:nth-child(1)")
    private WebElement errorMessage;

    @FindBy(css = "li:nth-child(2) svg")
    private WebElement cacheLink;

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public void clickCacheLink() {
        cacheLink.click();
    }
}

