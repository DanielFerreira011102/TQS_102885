package com.example.project.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ReservePage {

    @FindBy(css = "tr:nth-child(1) .btn")
    private WebElement chooseFlightButton;

    public ReservePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void clickChooseThisFlight() {
        chooseFlightButton.click();
    }
}