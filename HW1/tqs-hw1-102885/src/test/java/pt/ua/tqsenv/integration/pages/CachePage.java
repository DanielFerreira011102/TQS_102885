package pt.ua.tqsenv.integration.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CachePage {
    private final WebDriver driver;

    public CachePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "li:nth-child(1) > span")
    private WebElement requestCount;

    @FindBy(css = "li:nth-child(2) > span")
    private WebElement cacheHits;

    @FindBy(css = "li:nth-child(3) > span")
    private WebElement cacheMisses;

    @FindBy(css = "li:nth-child(4) > span")
    private WebElement expiredCount;

    @FindBy(css = "li:nth-child(1) svg")
    private WebElement searchLink;

    public String getRequestCount() {
        return requestCount.getText();
    }

    public String getCacheHits() {
        return cacheHits.getText();
    }

    public String getCacheMisses() {
        return cacheMisses.getText();
    }

    public String getExpiredCount() {
        return expiredCount.getText();
    }

    public void clickSearchLink() {
        searchLink.click();
    }
}
