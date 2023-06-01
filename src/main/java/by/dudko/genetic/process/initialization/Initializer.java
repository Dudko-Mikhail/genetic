package by.dudko.genetic.process.initialization;

import by.dudko.genetic.model.chromosome.Chromosome;

import java.util.stream.Stream;

public interface Initializer<T> {
    Stream<Chromosome<T>> produceChromosomes(int populationSize);
}
