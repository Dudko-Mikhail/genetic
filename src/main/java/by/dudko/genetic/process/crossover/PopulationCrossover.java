package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

public interface PopulationCrossover<G extends Gene<?, G>, F> {
    Population<G, F> performCrossover(Population<G, F> population, int offspringSize);
}
