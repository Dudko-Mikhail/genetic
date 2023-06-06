package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.Objects;
import java.util.random.RandomGenerator;

public class MonteCarloSelection<G extends Gene<?, G>, F> implements Selection<G, F> {
    private final RandomGenerator random;

    public MonteCarloSelection(RandomGenerator random) {
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public Population<G, F> select(Population<G, F> population, int selectedPopulationSize) {
        RequireUtils.positive(selectedPopulationSize);
        return new Population<>(RandomUtils.randomIndexes(random, 0, population.getSize(), selectedPopulationSize)
                .mapToObj(population::getIndividual)
                .toList());
    }
}
