package by.dudko.genetic.process.replacement;

import by.dudko.genetic.model.Population;

public interface Replacement<T, F> {
    Population<T, F> replace(Population<T, F> oldGeneration, Population<T, F> offspring, int newPopulationSize);
}
