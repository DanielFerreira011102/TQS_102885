package io.cucumber.skeleton;

import java.util.ArrayDeque;
import java.util.Deque;

public class Calculator {
    private final Deque<Double> stack = new ArrayDeque<>();

    public void push(Object arg) {
        if (arg instanceof String operator) {
            double y = stack.removeLast();
            double x = stack.isEmpty() ? 0 : stack.removeLast();
            double result = switch (operator) {
                case "-" -> x - y;
                case "+" -> x + y;
                case "*" -> x * y;
                case "/" -> x / y;
                default -> throw new IllegalArgumentException("Unknown operator: " + operator);
            };
            stack.add(result);
        } else if (arg instanceof Number number) {
            stack.add(number.doubleValue());
        } else {
            throw new IllegalArgumentException("Unsupported argument type: " + arg.getClass());
        }
    }

    public Number value() {
        return stack.getLast();
    }
}
