package by.dudko.genetic.algorithm.listeners;

import by.dudko.genetic.algorithm.GeneticAlgorithm;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

public interface CrossoverListener<G extends Gene<?, G>, F> {
    void beforeCrossover(GeneticAlgorithm<G, F> geneticAlgorithm, Population<G, F> parents);
}
