package by.dudko.genetic.algorithm.listeners;

import by.dudko.genetic.algorithm.GeneticAlgorithm;
import by.dudko.genetic.model.gene.Gene;

public interface SelectionListener<G extends Gene<?, G>, F> {
    void beforeSelection(GeneticAlgorithm<G, F> geneticAlgorithm);
}
