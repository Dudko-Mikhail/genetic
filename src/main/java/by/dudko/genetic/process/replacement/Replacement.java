package by.dudko.genetic.process.replacement;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

public interface Replacement<G extends Gene<?, G>, F> {
    Population<G, F> replace(Population<G, F> oldGeneration, Population<G, F> offspring, int newPopulationSize);
}
