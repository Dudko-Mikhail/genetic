package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.Population;

public interface PopulationMutation<T> {
    Population<T, ?> mutate(Population<T, ?> population);
}
