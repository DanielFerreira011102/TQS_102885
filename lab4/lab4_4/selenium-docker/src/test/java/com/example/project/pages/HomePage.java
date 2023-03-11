package com.example.project.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    private final WebDriver driver;

    @FindBy(name = "fromPort")
    private WebElement fromPortDropdown;

    @FindBy(name = "toPort")
    private WebElement toPortDropdown;

    @FindBy(css = ".btn-primary")
    private WebElement findFlightsButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void selectDepartureCity(String fromPort) {
        fromPortDropdown.click();
        driver.findElement(By.xpath("//option[. = '" + fromPort + "']")).click();
    }

    public void selectDestinationCity(String toPort) {
        toPortDropdown.click();
        driver.findElement(By.xpath("//option[. = '" + toPort + "']")).click();
    }

    public void clickFindFlights() {
        findFlightsButton.click();
    }
}
