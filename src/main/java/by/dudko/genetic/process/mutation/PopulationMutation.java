package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.Population;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface PopulationMutation<T> extends UnaryOperator<Population<T, ?>> {
}
