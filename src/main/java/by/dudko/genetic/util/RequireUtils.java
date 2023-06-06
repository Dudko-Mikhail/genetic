package by.dudko.genetic.util;

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

    public static double positive(double value) {
        if (Double.compare(value, 0.0) <= 0) {
            throw new IllegalArgumentException("Value must be positive. Actual value: %f".formatted(value));
        }
        return value;
    }

    public static int nonNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be >= 0. Actual value: %d".formatted(value));
        }
        return value;
    }

    public static int negative(int value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be negative. Actual value: %d".formatted(value));
        }
        return value;
    }

    public static int greater(int value, int bound) {
        if (bound > value) {
            throw new IllegalArgumentException("Value must be greater than bound. Value: %d, bound: %d"
                    .formatted(value, bound));
        }
        return value;
    }

    public static int less(int value, int bound) {
        if (value >= bound) {
            throw new IllegalArgumentException("Value must be less than bound. Value: %d, bound: %d"
                    .formatted(value, bound));
        }
        return value;
    }

    public static double less(double value, double bound) {
        if (Double.compare(value, bound) >= 0) {
            throw new IllegalArgumentException("Value must be less than bound. Value: %f, bound: %f"
                    .formatted(value, bound));
        }
        return value;
    }

    public static int lessOrEqual(int value, int bound) {
        if (value > bound) {
            throw new IllegalArgumentException("Value must be less than or equal to the bound. Value: %d, bound: %d"
                    .formatted(value, bound));
        }
        return value;
    }
}
