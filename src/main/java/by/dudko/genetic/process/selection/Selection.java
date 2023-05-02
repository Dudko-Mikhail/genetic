package by.dudko.genetic.process.selection;

import by.dudko.genetic.model.Population;

public interface Selection<T, F> {
    Population<T, F> select(Population<T, F> population, int selectedPopulationSize);
}
