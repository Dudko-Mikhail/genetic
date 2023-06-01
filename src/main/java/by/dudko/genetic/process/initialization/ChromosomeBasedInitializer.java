package by.dudko.genetic.process.initialization;

import by.dudko.genetic.model.chromosome.Chromosome;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ChromosomeBasedInitializer<T> implements Initializer<T> {
    private final Supplier<Chromosome<T>> chromosomeInitializer;

    public ChromosomeBasedInitializer(Supplier<Chromosome<T>> chromosomeInitializer) {
        this.chromosomeInitializer = Objects.requireNonNull(chromosomeInitializer);
    }

    @Override
    public final Stream<Chromosome<T>> produceChromosomes(int populationSize) {
        return Stream.generate(chromosomeInitializer)
                .limit(populationSize);
    }
}