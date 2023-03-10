package com.example.project;// Generated by Selenium IDE

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SeleniumJupiter.class)
public class BlazerunnerTestDI {

  @Test
  public void blazerunner(ChromeDriver driver) {
    // Test name: Blazerunner
    // Step # | name | target | value
    // 1 | open | / | 
    driver.get("https://blazedemo.com/");
    // 2 | setWindowSize | 1042x802 | 
    driver.manage().window().setSize(new Dimension(1042, 802));
    // 3 | click | name=fromPort | 
    driver.findElement(By.name("fromPort")).click();
    // 4 | select | name=fromPort | label=Boston
    {
      WebElement dropdown = driver.findElement(By.name("fromPort"));
      dropdown.findElement(By.xpath("//option[. = 'Boston']")).click();
    }
    // 5 | click | name=toPort | 
    driver.findElement(By.name("toPort")).click();
    // 6 | select | name=toPort | label=Berlin
    {
      WebElement dropdown = driver.findElement(By.name("toPort"));
      dropdown.findElement(By.xpath("//option[. = 'Berlin']")).click();
    }
    // 7 | click | css=.btn-primary | 
    driver.findElement(By.cssSelector(".btn-primary")).click();
    // 8 | click | css=tr:nth-child(1) .btn | 
    driver.findElement(By.cssSelector("tr:nth-child(1) .btn")).click();
    // 9 | click | id=inputName | 
    driver.findElement(By.id("inputName")).click();
    // 10 | type | id=inputName | Johnny
    driver.findElement(By.id("inputName")).sendKeys("Johnny");
    // 11 | type | id=address | 123 Street
    driver.findElement(By.id("address")).sendKeys("123 Street");
    // 12 | type | id=city | SomeTown
    driver.findElement(By.id("city")).sendKeys("SomeTown");
    // 13 | type | id=state | New state
    driver.findElement(By.id("state")).sendKeys("New state");
    // 14 | type | id=zipCode | 9876
    driver.findElement(By.id("zipCode")).sendKeys("9876");
    // 15 | type | id=creditCardNumber | 4
    driver.findElement(By.id("creditCardNumber")).sendKeys("4");
    // 16 | type | id=creditCardYear | 2022
    driver.findElement(By.id("creditCardYear")).sendKeys("2022");
    // 17 | type | id=nameOnCard | John Michael
    driver.findElement(By.id("nameOnCard")).sendKeys("John Michael");
    // 18 | click | css=.checkbox | 
    driver.findElement(By.cssSelector(".checkbox")).click();
    // 19 | click | css=.btn-primary | 
    driver.findElement(By.cssSelector(".btn-primary")).click();
    // 20 | assertText | css=tr:nth-child(3) > td:nth-child(2) | 555 USD
    // Blazerunning right price gameshow
    assertThat(driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(2)")).getText()).isEqualTo("555 USD");
    // 21 | assertTitle | BlazeDemo Confirmation | 
    // Blazerunning title confirmation
    assertThat(driver.getTitle()).isEqualTo("BlazeDemo Confirmation");
  }
}
