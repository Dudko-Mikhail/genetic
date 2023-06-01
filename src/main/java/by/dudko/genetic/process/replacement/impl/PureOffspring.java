package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.process.selection.Selection;

public class PureOffspring<T, F> extends AbstractReplacement<T, F> {
    public PureOffspring(Selection<T, F> selection) {
        super(selection);
    }

    @Override
    protected Population<T, F> union(Population<T, F> oldGeneration, Population<T, F> offspring) {
        return offspring;
    }
}
