package io.cucumber.skeleton;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class Utils {
    public static <T> T mapToObject(Map<String, String> map, Class<T> clazz) {
        T obj; // create a new instance of the target class
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);

        // iterate through all fields in the target class
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName(); // get the name of the field
            String fieldValue = map.get(fieldName); // get the value of the field from the map

            if (fieldValue != null) { // if the field value exists in the map
                field.setAccessible(true); // allow access to private fields
                Class<?> fieldType = field.getType(); // get the type of the field
                Object convertedValue = switch (fieldType.getSimpleName()) {
                    case "String" -> fieldValue;
                    case "int", "Integer" -> Integer.parseInt(fieldValue);
                    case "double", "Double" -> Double.parseDouble(fieldValue);
                    case "boolean", "Boolean" -> Boolean.parseBoolean(fieldValue);
                    case "LocalDate" -> LocalDate.parse(fieldValue, formatter);
                    default -> null; // the converted value of the field

                    // convert the string value of the field to the appropriate type
                    // add cases for other types as needed
                };

                try {
                    field.set(obj, convertedValue); // set the converted value of the field in the object
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return obj;
    }
}