package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.selection.Selection;

import java.util.Objects;

public abstract class UnaryReplacement<G extends Gene<?, G>, F> implements Replacement<G, F> {
    private final Selection<G, F> selection;

    protected UnaryReplacement(Selection<G, F> selection) {
        this.selection = Objects.requireNonNull(selection);
    }

    @Override
    public final Population<G, F> replace(Population<G, F> oldGeneration, Population<G, F> offspring,
                                          int newPopulationSize) {
        return selection.select(union(oldGeneration, offspring), newPopulationSize);
    }

    protected abstract Population<G, F> union(Population<G, F> oldGeneration, Population<G, F> offspring);
}
