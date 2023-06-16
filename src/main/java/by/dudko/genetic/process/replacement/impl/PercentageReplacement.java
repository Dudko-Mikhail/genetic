package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RequireUtils;

public class PercentageReplacement<G extends Gene<?, G>, F> extends BinaryReplacement<G, F> {
    private final double offspringPercentage;

    public static <G extends Gene<?, G>, F> Replacement<G, F> pureOffspring(Selection<G, F> offspringSelection) {
        return new PercentageReplacement<>(1, offspringSelection);
    }

    public PercentageReplacement(double offspringPercentage,
                                 Selection<G, F> oldGenerationSelection, Selection<G, F> offspringSelection) {
        super(oldGenerationSelection, offspringSelection);
        this.offspringPercentage = RequireUtils.probability(offspringPercentage);
    }

    public PercentageReplacement(double offspringPercentage,
                                 Selection<G, F> selection) {
        super(selection);
        this.offspringPercentage = RequireUtils.probability(offspringPercentage);
    }

    @Override
    protected int defineOffspringSizeInNewPopulation(int oldGenerationSize, int newPopulationSize) {
        return (int) (newPopulationSize * offspringPercentage);
    }
}
