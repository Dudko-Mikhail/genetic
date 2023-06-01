package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RequireUtils;

public class PercentageReplacement<T, F> extends AbstractBinaryReplacement<T, F> {
    private final double offspringPercentage;

    public PercentageReplacement(double offspringPercentage,
                                 Selection<T, F> oldGenerationSelection, Selection<T, F> offspringSelection) {
        super(oldGenerationSelection, offspringSelection);
        this.offspringPercentage = RequireUtils.probability(offspringPercentage);
    }

    public PercentageReplacement(double offspringPercentage,
                                 Selection<T, F> selection) {
        super(selection);
        this.offspringPercentage = RequireUtils.probability(offspringPercentage);
    }

    @Override
    protected int defineOffspringSizeInNewPopulation(int oldGenerationSize, int newPopulationSize) {
        return (int) (newPopulationSize * offspringPercentage);
    }
}
