package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.RequireUtils;

import java.util.Comparator;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class LinearRankSelection<G extends Gene<?, G>, F> extends ProbabilitySelection<G, F> {
    private final Comparator<? super F> comparator;
    private final double a;
    private final double b;

    // todo remove Чем больше a/n - вероятность выбора худшего индивида
    public LinearRankSelection(RandomGenerator random, Comparator<? super F> comparator) {
        this(random, comparator, 0.5);
    }

    public LinearRankSelection(RandomGenerator random, Comparator<? super F> comparator, double a) {
        super(random);
        if (Double.compare(a, 0) <= 0 || Double.compare(a, 2) >= 0) {
            throw new IllegalArgumentException("A must be in the range (0, 2). Actual value is: %f".formatted(a));
        }
        this.a = a;
        this.b = 2 - 2 * a;
        this.comparator = Objects.requireNonNull(comparator).reversed();
    }

    @Override
    protected double[] calculateProbabilities(Population<G, F> population) {
        int n = population.getSize();
        int nMinus1 = n - 1;
        double[] probabilities = new double[n];
        population.sort(comparator);
        for (int i = 0; i < n; i++) {
            probabilities[i] = (a + (b * (nMinus1 - i) / nMinus1)) / n;
        }
        return probabilities;
    }
}
