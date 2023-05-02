package by.dudko.genetic.stepProcess.simplegeneralization;

import by.dudko.genetic.model.chromosome.Chromosome;

import java.util.function.Supplier;

public interface ChromosomeInitializer<T> extends Supplier<Chromosome<T>> {
}
