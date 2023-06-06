package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RequireUtils;

import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public abstract class ProbabilitySelection<G extends Gene<?, G>, F> implements Selection<G, F> { // todo проблема отрицательной приспособленности
    private static final int BINARY_SEARCH_THRESHOLD = 32;
    protected final RandomGenerator random;

    protected ProbabilitySelection(RandomGenerator random) {
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public final Population<G, F> select(Population<G, F> population, int selectedPopulationSize) {
        RequireUtils.positive(selectedPopulationSize);
        double[] partialSums = mapToPartialSums(calculateProbabilities(population));
        return select(population, selectedPopulationSize, partialSums);
    }

    public Population<G, F> select(Population<G, F> population, int selectedPopulationSize, double[] partialSums) {
        int size = population.getSize();
        var selectedIndividuals = Stream.generate(() -> population.getIndividual(spin(partialSums) % size))
                .limit(selectedPopulationSize)
                .toList();
        return new Population<>(selectedIndividuals, population.getFitnessFunction());
    }

    protected double[] mapToPartialSums(double[] probabilities) { // todo change to private
        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            probabilities[i] = sum;
        }
        return probabilities;
    }

    protected abstract double[] calculateProbabilities(Population<G, F> population);

    protected int spin(double[] partialSums) {
        double value = random.nextDouble();
        return find(partialSums, value, 0);
    }

    protected int find(double[] partialSums, double value, int start) {
        return partialSums.length < BINARY_SEARCH_THRESHOLD ? linearSearch(partialSums, value, start)
                : binarySearch(partialSums, value, start);
    }

    int linearSearch(double[] partialSums, double value, int start) { // todo package-private Для тестирования
        for (int i = start; i < partialSums.length; i++) {
            if (value <= partialSums[i]) {
                return i;
            }
        }
        return partialSums.length - 1;
    }

    int binarySearch(double[] partialSums, double value, int start) { // todo package-private Для тестирования
        int end = partialSums.length - 1;
        int begin = start;
        while (begin <= end) {
            int mid = (begin + end) >>> 1;
            double midValue = partialSums[mid];

            if (midValue < value)
                begin = mid + 1;
            else if (midValue > value)
                end = mid - 1;
            else
                return mid;
        }

        if (begin - 1 >= 0 && value < partialSums[begin - 1] && begin - 1 >= start) {
            System.out.println("Here " + value);
            return start - 1;
        }
        return Math.min(begin, partialSums.length - 1);
    }
}
