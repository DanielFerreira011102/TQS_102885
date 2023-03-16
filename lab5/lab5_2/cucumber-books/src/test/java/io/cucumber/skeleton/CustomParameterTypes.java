package io.cucumber.skeleton;

import io.cucumber.java.ParameterType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomParameterTypes {
    @ParameterType(".*")
    public LocalDate localDate(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
        return LocalDate.parse(input, formatter);
    }
}
