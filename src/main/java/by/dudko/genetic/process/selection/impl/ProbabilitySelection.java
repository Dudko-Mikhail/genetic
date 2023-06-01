package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RequireUtils;

import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public abstract class ProbabilitySelection<T, F> implements Selection<T, F> { // todo проблема отрицательной приспособленности
    private static final int BINARY_SEARCH_THRESHOLD = 32;
    protected final RandomGenerator random;

    protected ProbabilitySelection(RandomGenerator random) {
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public Population<T, F> select(Population<T, F> population, int selectedPopulationSize) {
        RequireUtils.positive(selectedPopulationSize);
        double[] partialSums = mapToPartialSums(calculateProbabilities(population));
        int size = population.getSize();
        var selectedIndividuals = Stream.generate(() -> population.getIndividual(spin(partialSums) % size))
                .limit(selectedPopulationSize)
                .toList();
        return new Population<>(selectedIndividuals, population.getFitnessFunction());
    }

    public abstract double[] calculateProbabilities(Population<T, F> population);

    public int spin(double[] partialSums) {
        double value = random.nextDouble();
        return partialSums.length < BINARY_SEARCH_THRESHOLD ? linearSearch(partialSums, value)
                : binarySearch(partialSums, value);
    }

    int linearSearch(double[] partialSums, double value) {
        for (int i = 0; i < partialSums.length; i++) {
            if (value < partialSums[i]) {
                return i;
            }
        }
        return 0;
    }

    int binarySearch(double[] partialSums, double value) {
        int start = 0;
        int end = partialSums.length - 1;

        while (start <= end) {
            int mid = (start + end) >>> 1;
            double midValue = partialSums[mid];

            if (midValue < value)
                start = mid + 1;
            else if (midValue > value)
                end = mid - 1;
            else
                return mid;
        }

        if (start - 1 >= 0 && value < partialSums[start - 1]) {
            System.out.println("Here " + value);
            return start - 1;
        }
        return Math.min(start, partialSums.length - 1);
    }

    private double[] mapToPartialSums(double[] probabilities) {
        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            probabilities[i] = sum;
        }
        return probabilities;
    }

    public static void main(String[] args) { // todo Делать ли что-то при неудачном поиске
        double[] arr = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        var selection = new ProbabilitySelection(new Random()) {
            @Override
            public double[] calculateProbabilities(Population population) {
                return new double[0];
            }

        };
        double toSearch = -0.11;
        for (int i = 0; i < 14; i++) {
            var index = selection.binarySearch(arr, toSearch);
            System.out.println("Index: [%d]. Searched value: [%f].  Answer: [%f]%n"
                    .formatted(index, toSearch, arr[index]));
            System.out.println("Linear search. Index: [%d]. Searched value: [%f].  Answer: [%f]%n"
                    .formatted(selection.linearSearch(arr, toSearch), toSearch, arr[selection.linearSearch(arr, toSearch)]));
            toSearch += 0.1;
        }
    }
}
