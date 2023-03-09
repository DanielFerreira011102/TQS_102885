package com.example.project.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PurchasePage {

    @FindBy(id = "inputName")
    private WebElement inputNameField;

    @FindBy(id = "address")
    private WebElement addressField;

    @FindBy(id = "city")
    private WebElement cityField;

    @FindBy(id = "state")
    private WebElement stateField;

    @FindBy(id = "zipCode")
    private WebElement zipCodeField;

    @FindBy(id = "creditCardNumber")
    private WebElement creditCardNumberField;

    @FindBy(id = "creditCardYear")
    private WebElement creditCardYearField;

    @FindBy(id = "nameOnCard")
    private WebElement nameOnCardField;

    @FindBy(css = ".checkbox")
    private WebElement rememberMeCheckbox;

    @FindBy(css = ".btn-primary")
    private WebElement purchaseFlightButton;

    public PurchasePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void fillOutPersonalInformation(String name, String address, String city, String state, String zipCode) {
        inputNameField.sendKeys(name);
        addressField.sendKeys(address);
        cityField.sendKeys(city);
        stateField.sendKeys(state);
        zipCodeField.sendKeys(zipCode);
    }

    public void fillOutPaymentInformation(String creditCardNumber, String creditCardYear, String nameOnCard) {
        creditCardNumberField.sendKeys(creditCardNumber);
        creditCardYearField.sendKeys(creditCardYear);
        nameOnCardField.sendKeys(nameOnCard);
    }

    public void clickRememberMe() {
        rememberMeCheckbox.click();
    }

    public void clickPurchaseFlight() {
        purchaseFlightButton.click();
    }
}