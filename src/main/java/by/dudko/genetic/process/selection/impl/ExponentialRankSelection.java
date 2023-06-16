package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.BooleanGene;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.evaluation.FitnessFunction;
import by.dudko.genetic.process.initialization.ChromosomeBasedInitializer;
import by.dudko.genetic.process.initialization.GeneBasedInitializer;
import by.dudko.genetic.process.initialization.Initializer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;

import static java.lang.Math.pow;

public class ExponentialRankSelection<G extends Gene<?, G>, F> extends ProbabilitySelection<G, F> {
    private final double k;
    private final Comparator<? super F> comparator;

    public ExponentialRankSelection(RandomGenerator random, Comparator<? super F> comparator, double k) {
        super(random);
        this.comparator = Objects.requireNonNull(comparator).reversed();
        if (Double.compare(k, 0) < 0 || Double.compare(k, 1) >= 0) {
            throw new IllegalArgumentException("K must be in the range [0, 1). Actual value is: %f".formatted(k));
        }
        this.k = k;
    }

    @Override
    protected double[] calculateProbabilities(Population<G, F> population) {
        int n = population.getSize();
        double[] probabilities = new double[n];
        population.sort(comparator);
        final double b = (k - 1.0) / (pow(k, n) - 1.0);
        for (int i = 0; i < n; ++i) {
            probabilities[i] = pow(k, i) * b;
        }
        return probabilities;
    }

    public static void main(String[] args) { // todo remove
        Random random = new Random();
        Initializer<BooleanGene> initializer = new ChromosomeBasedInitializer<>(
                new GeneBasedInitializer<>(
                        () -> new BooleanGene(random.nextBoolean()),
                        () -> 10
                )
        );
        FitnessFunction<BooleanGene, Long> fitnessFunction = (chromosome) ->
                chromosome.getGenes()
                        .stream()
                        .map(BooleanGene::getValue)
                        .filter(value -> value)
                        .count();
        Population<BooleanGene, Long> population = new Population<>(initializer.produceChromosomes(10).toList(), fitnessFunction);
        population.evaluatePopulation();

        LinearRankSelection<BooleanGene, Long> linearRankSelection = new LinearRankSelection<>(random, Long::compare, 1.9);
        ExponentialRankSelection<BooleanGene, Long> selection = new ExponentialRankSelection<>(random, Long::compareTo, 0.9);


        var probabilities = linearRankSelection.calculateProbabilities(population);
        System.out.println(population.getIndividuals()
                .stream().map(Individual::getFitness)
                .toList());
        System.out.println("Sum = " + Arrays.stream(probabilities).reduce(Double::sum));
        System.out.println("Probabilities " + Arrays.toString(probabilities));
        System.out.println("Partition sums " + Arrays.toString(selection.mapToPartialSums(probabilities)));
    }
}
