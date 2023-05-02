package by.dudko.genetic.process.initialization;

import by.dudko.genetic.model.chromosome.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface Initializer<T> {
    default List<Chromosome<T>> initialize(int populationSize) {
        List<Chromosome<T>> chromosomes = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            chromosomes.add(produceChromosome());
        }
        return chromosomes;
    }

    default Stream<Chromosome<T>> produceChromosomes(int populationSize) {
        return Stream.generate(this::produceChromosome)
                .limit(populationSize);
    }

    Chromosome<T> produceChromosome();
}
