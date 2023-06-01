package by.dudko.genetic.process.evaluation;

import by.dudko.genetic.model.Population;

import java.util.function.Predicate;

public interface StopFunction<F> extends Predicate<Population<?, F>> {
}
