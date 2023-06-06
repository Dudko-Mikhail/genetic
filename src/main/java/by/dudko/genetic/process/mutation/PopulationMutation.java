package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

import java.util.function.UnaryOperator;

public interface PopulationMutation<G extends Gene<?, G>> extends UnaryOperator<Population<G, ?>> {
}
