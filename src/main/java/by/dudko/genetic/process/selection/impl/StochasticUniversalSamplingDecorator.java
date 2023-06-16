package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class StochasticUniversalSamplingDecorator<G extends Gene<?, G>, F> extends ProbabilitySelection<G, F> {
    private final int pointers;
    private final double distance;
    private final ProbabilitySelection<G, F> selection;

    public StochasticUniversalSamplingDecorator(RandomGenerator random, int pointers,
                                                ProbabilitySelection<G, F> selection) {
        super(random);
        this.pointers = RequireUtils.positive(pointers);
        distance = 1.0 / pointers;
        this.selection = Objects.requireNonNull(selection);
    }

    @Override
    protected Population<G, F> select(Population<G, F> population, int selectedPopulationSize, double[] partialSums) {
        List<Individual<G, F>> individuals = new ArrayList<>(selectedPopulationSize);
        while (individuals.size() < selectedPopulationSize) {
            double pointerPosition = random.nextDouble(distance);
            for (int i = 0, j = 0; i < pointers; i++) {
                j = find(partialSums, pointerPosition, j);
                pointerPosition += distance;
                individuals.add(population.getIndividual(j));
                if (individuals.size() == selectedPopulationSize) {
                    break;
                }
            }
        }
        return new Population<>(individuals, population.getFitnessFunction());
    }

    @Override
    protected double[] calculateProbabilities(Population<G, F> population) {
        return this.selection.calculateProbabilities(population);
    }
}