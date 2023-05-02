package by.dudko.genetic.stepProcess.additionalgeneralization;

import by.dudko.genetic.model.chromosome.Chromosome;

import java.util.stream.Stream;

public interface PopulationInitializer<V, C extends Chromosome<V>> {
    Stream<C> initialize(int populationSize);

    default Stream<Chromosome<V>> boxed(int size) {
        return initialize(size)
                .map(chromosome -> chromosome);
    }
}
