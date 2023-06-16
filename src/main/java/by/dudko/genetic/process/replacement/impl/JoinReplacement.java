package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.selection.Selection;

public class JoinReplacement<G extends Gene<?, G>, F> extends UnaryReplacement<G, F> {
    public JoinReplacement(Selection<G, F> selection) {
        super(selection);
    }

    @Override
    protected Population<G, F> union(Population<G, F> oldGeneration, Population<G, F> offspring) {
        oldGeneration.addAllWithEvaluation(offspring.getIndividuals());
        return oldGeneration;
    }
}
