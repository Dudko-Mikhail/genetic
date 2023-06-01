package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.selection.Selection;

import java.util.Objects;

public abstract class AbstractReplacement<T, F> implements Replacement<T, F> {
    private final Selection<T, F> selection;

    protected AbstractReplacement(Selection<T, F> selection) {
        this.selection = Objects.requireNonNull(selection);
    }

    @Override
    public final Population<T, F> replace(Population<T, F> oldGeneration, Population<T, F> offspring, int newPopulationSize) {
        return selection.select(union(oldGeneration, offspring), newPopulationSize);
    }

    protected abstract Population<T, F> union(Population<T, F> oldGeneration, Population<T, F> offspring);
}
