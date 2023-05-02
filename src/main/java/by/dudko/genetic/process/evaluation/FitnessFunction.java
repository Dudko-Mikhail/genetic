package by.dudko.genetic.process.evaluation;

import by.dudko.genetic.model.chromosome.Chromosome;

import java.util.function.Function;

public interface FitnessFunction<T, F> extends Function<Chromosome<T>, F> { // todo подумать о wildcards в параметрах
}
