package io.cucumber.skeleton;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {

    private Calculator calc;

    @Given("A brand new $5000 calculator")
    public void setup() {
        calc = new Calculator();
    }

    @When("I add {int} and {int}")
    public void add(int arg1, int arg2) {
        calc.push(arg1);
        calc.push(arg2);
        calc.push("+");
    }

    @When("I subtract {int} to {int}")
    public void subtract(int arg1, int arg2) {
        calc.push(arg1);
        calc.push(arg2);
        calc.push("-");
    }

    @Then("The result is {int}")
    public void the_result_is(double expected) {
        assertEquals(expected, calc.value());
    }
}
