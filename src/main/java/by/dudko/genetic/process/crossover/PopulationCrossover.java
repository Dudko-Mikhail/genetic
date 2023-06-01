package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.Population;

public interface PopulationCrossover<T> {
    Population<T, ?> performCrossover(Population<T, ?> population, int offspringSize);
}
