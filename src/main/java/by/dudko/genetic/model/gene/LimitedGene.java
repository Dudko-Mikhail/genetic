package by.dudko.genetic.model.gene;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.process.crossover.PopulationCrossover;

import java.util.Comparator;

public interface LimitedGene<T, G extends LimitedGene<T, G>> extends Gene<T, G> {
    static <T> boolean isInBounds(T value, T min, T max, Comparator<T> comparator) {
        return comparator.compare(value, min) >= 0 && comparator.compare(value, max) <= 0;
    }

    T min();

    T max();

    default boolean isInBounds(T value) {
        return isInBounds(value, min(), max());
    }

    boolean isInBounds(T value, T min, T max);

    G newInstance(T value, T min, T max);

    G newInstanceWithDefaultBounds(T value);
}