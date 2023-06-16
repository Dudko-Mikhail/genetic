package by.dudko.genetic.process.initialization;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ChromosomeBasedInitializer<G extends Gene<?, G>> implements Initializer<G> {
    private final Supplier<Chromosome<G>> chromosomeInitializer;

    public ChromosomeBasedInitializer(Supplier<Chromosome<G>> chromosomeInitializer) {
        this.chromosomeInitializer = Objects.requireNonNull(chromosomeInitializer);
    }

    @Override
    public final Stream<Chromosome<G>> produceChromosomes(int populationSize) {
        return Stream.generate(chromosomeInitializer)
                .limit(populationSize);
    }
}
