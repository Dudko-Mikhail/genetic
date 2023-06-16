package by.dudko.genetic.algorithm.listeners;

import by.dudko.genetic.algorithm.GeneticAlgorithm;
import by.dudko.genetic.model.gene.Gene;

public interface NextGenerationListener<G extends Gene<?, G>, F> {
    void beforeNextGeneration(GeneticAlgorithm<G, F> geneticAlgorithm);
}
