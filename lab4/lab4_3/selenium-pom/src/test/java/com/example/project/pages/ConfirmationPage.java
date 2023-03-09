package com.example.project.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ConfirmationPage {

    private final WebDriver driver;

    @FindBy(css = "tr:nth-child(3) > td:nth-child(2)")
    private WebElement price;

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getPrice() {
        return price.getText();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
