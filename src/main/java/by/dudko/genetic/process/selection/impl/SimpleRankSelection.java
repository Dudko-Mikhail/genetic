package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

import java.util.Comparator;
import java.util.random.RandomGenerator;

public class SimpleRankSelection<G extends Gene<?, G>, F> extends ProbabilitySelection<G, F> {
    private final Comparator<? super F> comparator;

    public SimpleRankSelection(RandomGenerator random, Comparator<? super F> comparator) {
        super(random);
        this.comparator = comparator;
    }

    @Override
    protected double[] calculateProbabilities(Population<G, F> population) {
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
