package by.dudko.genetic.process.initialization;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.BaseChromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GeneBasedInitializer<G extends Gene<?, G>> implements Supplier<Chromosome<G>>, Initializer<G> {
    private final Supplier<G> geneInitializer;
    private final Supplier<Integer> lengthSupplier;

    public GeneBasedInitializer(Supplier<G> geneInitializer, Supplier<Integer> lengthSupplier) {
        this.geneInitializer = Objects.requireNonNull(geneInitializer);
        this.lengthSupplier = Objects.requireNonNull(lengthSupplier);
    }

    @Override
    public Stream<Chromosome<G>> produceChromosomes(int populationSize) { // todo как тебе такое?
        return new ChromosomeBasedInitializer<>(this)
                .produceChromosomes(populationSize);
    }

    @Override
    public final Chromosome<G> get() { // todo Завязка на конкретную реализацию
        return new BaseChromosome<>(
                Stream.generate(geneInitializer)
                        .limit(lengthSupplier.get())
                        .toList()
        );
    }
}
