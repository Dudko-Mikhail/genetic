package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;

import java.util.Comparator;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class RankSelection<T, F> extends ProbabilitySelection<T, F> {
    private final Comparator<? super F> comparator;

    public RankSelection(RandomGenerator random, Comparator<? super F> comparator) {
        super(random);
        this.comparator = Objects.requireNonNull(comparator);
    }

    @Override
    public double[] calculateProbabilities(Population<T, F> population) {
        int length = population.getSize();
        double sum = (1.0 + length) / 2 * length;  // sum of arithmetic progression
        double[] probabilities = new double[length];
        population.sort(comparator);
        for (int i = 0; i < length; i++) {
            probabilities[i] = (i + 1.0) / sum;
        }
        return probabilities;
    }
}
