package by.dudko.genetic.process.evaluation;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.function.Function;

public interface FitnessFunction<G extends Gene<?, G>, F> extends Function<Chromosome<G>, F> {
}
