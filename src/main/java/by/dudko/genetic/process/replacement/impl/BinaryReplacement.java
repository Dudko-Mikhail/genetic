package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.Objects;

public abstract class BinaryReplacement<G extends Gene<?, G>, F> implements Replacement<G, F> {
    private final Selection<G, F> oldGenerationSelection;
    private final Selection<G, F> offspringSelection;

    protected BinaryReplacement(Selection<G, F> selection) {
        this(selection, selection);
    }

    protected BinaryReplacement(Selection<G, F> oldGenerationSelection, Selection<G, F> offspringSelection) {
        this.oldGenerationSelection = Objects.requireNonNull(oldGenerationSelection);
        this.offspringSelection = Objects.requireNonNull(offspringSelection);
    }

    @Override
    public final Population<G, F> replace(Population<G, F> oldGeneration, Population<G, F> offspring,
                                          int newPopulationSize) {
        RequireUtils.positive(newPopulationSize);
        int offSpringSize = defineOffspringSizeInNewPopulation(oldGeneration.getSize(), newPopulationSize)
                % newPopulationSize;
        int oldGenerationSize = newPopulationSize - offSpringSize;
        var newGenerationIndividuals = new ArrayList<>(oldGenerationSelection.select(oldGeneration, oldGenerationSize)
                .getIndividuals());
        newGenerationIndividuals.addAll(offspringSelection.select(offspring, offSpringSize).getIndividuals());
        return new Population<>(newGenerationIndividuals, oldGeneration.getFitnessFunction());
    }

    protected abstract int defineOffspringSizeInNewPopulation(int oldGenerationSize, int newPopulationSize);
}
