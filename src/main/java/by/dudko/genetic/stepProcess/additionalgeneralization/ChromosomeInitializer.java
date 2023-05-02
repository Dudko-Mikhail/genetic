package by.dudko.genetic.stepProcess.additionalgeneralization;

import by.dudko.genetic.model.chromosome.Chromosome;

import java.util.function.Supplier;

public interface ChromosomeInitializer<V, C extends Chromosome<V>> extends Supplier<C> {
}
