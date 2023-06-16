package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RequireUtils;

import java.util.Comparator;
import java.util.Objects;

public class TruncationSelection<G extends Gene<?, G>, F> implements Selection<G, F> {
    private final Comparator<? super F> comparator;

    public TruncationSelection(Comparator<? super F> comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    @Override
    public Population<G, F> select(Population<G, F> population, int selectedPopulationSize) {
        RequireUtils.positive(selectedPopulationSize);
        population.sort(comparator.reversed());
        var selectedIndividuals = population.getIndividuals()
                .stream()
                .limit(selectedPopulationSize)
                .toList();
        return new Population<>(selectedIndividuals, population.getFitnessFunction());
    }
}
