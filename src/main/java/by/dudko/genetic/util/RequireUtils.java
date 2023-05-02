package by.dudko.genetic.util;

import java.util.Objects;

public final class RequireUtils { // todo refactor messages. Change class name to NumberUtils
    private RequireUtils() {
    }

    public static double probability(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("The probability must be in [0, 1] interval. Actual value is %f"
                    .formatted(probability));
        }
        return probability;
    }

    public static int positive(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be positive. Actual value: %d".formatted(value));
        }
        return value;
    }

    public static int negative(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be negative. Actual value: %d".formatted(value));
        }
        return value;
    }

    public static int greater(int value, int bound) {
        if (bound > value) {
            throw new IllegalArgumentException("Value must be greater than bound. Value: %d, bound: %d"
                    .formatted(value, bound));
        }
        return bound;
    }

    public static int less(int value, int bound) {
        if (value >= bound) {
            throw new IllegalArgumentException("Value must be less than bound. Value: %d, bound: %d"
                    .formatted(value, bound));
        }
        return bound;
    }

    public static int lessOrEqual(int value, int bound) {
        if (value > bound) {
            throw new IllegalArgumentException("Value must be less than or equal to the bound. Value: %d, bound: %d"
                    .formatted(value, bound));
        }
        return bound;
    }

    public static <T> T requireComparable(T object) {
        if (!Comparable.class.isAssignableFrom(Objects.requireNonNull(object.getClass()))) {
            throw new IllegalArgumentException();
        }
        return object;
    }
}
