package by.dudko.genetic.statistics;

import by.dudko.genetic.model.Population;

import java.util.function.Function;

public interface Metric<T, G, F> extends Function<Population<G, F>, T> {
    String name();

    T result();
}
