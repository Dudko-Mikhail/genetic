package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.Objects;

public abstract class AbstractBinaryReplacement<T, F> implements Replacement<T, F> {
    private final Selection<T, F> oldGenerationSelection;
    private final Selection<T, F> offspringSelection;

    protected AbstractBinaryReplacement(Selection<T, F> selection) {
        this(selection, selection);
    }

    protected AbstractBinaryReplacement(Selection<T, F> oldGenerationSelection, Selection<T, F> offspringSelection) {
        this.oldGenerationSelection = Objects.requireNonNull(oldGenerationSelection);
        this.offspringSelection = Objects.requireNonNull(offspringSelection);
    }

    @Override
    public final Population<T, F> replace(Population<T, F> oldGeneration, Population<T, F> offspring, int newPopulationSize) {
        RequireUtils.positive(newPopulationSize);
        int offSpringSize = defineOffspringSizeInNewPopulation(oldGeneration.getSize(), newPopulationSize);
        int oldGenerationSize = newPopulationSize - offSpringSize;
        var oldGenerationIndividuals = oldGenerationSelection.select(oldGeneration, oldGenerationSize);
        var offSpringIndividuals = offspringSelection.select(offspring, offSpringSize);
        var newGenerationIndividuals = new ArrayList<>(oldGenerationIndividuals.getIndividuals());
        newGenerationIndividuals.addAll(offSpringIndividuals.getIndividuals());
        return new Population<>(newGenerationIndividuals, oldGeneration.getFitnessFunction());
    }

    protected abstract int defineOffspringSizeInNewPopulation(int oldGenerationSize, int newPopulationSize);
}
