package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.selection.Selection;

public class PureOffspring<G extends Gene<?, G>, F> extends AbstractReplacement<G, F> {
    public PureOffspring(Selection<G, F> selection) {
        super(selection);
    }

    @Override
    protected Population<G, F> union(Population<G, F> oldGeneration, Population<G, F> offspring) {
        return offspring;
    }
}
