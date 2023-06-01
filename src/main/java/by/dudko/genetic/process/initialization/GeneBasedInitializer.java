package by.dudko.genetic.process.initialization;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.BaseChromosome;
import by.dudko.genetic.model.gene.BaseGene;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GeneBasedInitializer<T> implements Supplier<Chromosome<T>>, Initializer<T> {
    private final Supplier<BaseGene<T>> geneInitializer;
    private final Supplier<Integer> lengthSupplier;

    public GeneBasedInitializer(Supplier<BaseGene<T>> geneInitializer, Supplier<Integer> lengthSupplier) {
        this.geneInitializer = Objects.requireNonNull(geneInitializer);
        this.lengthSupplier = Objects.requireNonNull(lengthSupplier);
    }

    @Override
    public Stream<Chromosome<T>> produceChromosomes(int populationSize) { // todo как тебе такое?
        return new ChromosomeBasedInitializer<>(this)
                .produceChromosomes(populationSize);
    }

    @Override
    public final Chromosome<T> get() { // todo Завязка на конкретную реализацию
        return new BaseChromosome<>(
                Stream.generate(geneInitializer)
                        .limit(lengthSupplier.get())
                        .toList()
        );
    }
}
