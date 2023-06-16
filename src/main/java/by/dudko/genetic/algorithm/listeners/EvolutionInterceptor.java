package by.dudko.genetic.algorithm.listeners;

import by.dudko.genetic.model.gene.Gene;

public interface EvolutionInterceptor<G extends Gene<?, G>, F> extends SelectionListener<G, F>,
        CrossoverListener<G, F>, MutationListener<G, F>, ReplacementListener<G, F> {
}
