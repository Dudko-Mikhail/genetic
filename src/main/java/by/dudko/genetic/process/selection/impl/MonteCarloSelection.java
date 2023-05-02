package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RandomUtils;

import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public class MonteCarloSelection<T, F> implements Selection<T, F> {
    private final RandomGenerator random;

    public MonteCarloSelection(RandomGenerator random) {
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public Population<T, F> select(Population<T, F> population, int selectedPopulationSize) {
        
        return null;
    }
}
