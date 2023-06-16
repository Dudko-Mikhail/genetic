package by.dudko.genetic.process.initialization;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.BaseChromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.RequireUtils;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public class GeneBasedInitializer<G extends Gene<?, G>> implements Supplier<Chromosome<G>>, Initializer<G> {
    private final Supplier<G> geneInitializer;
    private final Supplier<Integer> lengthProvider;

    public GeneBasedInitializer(Supplier<G> geneInitializer, RandomGenerator random, int minLength, int maxLength) {
        this.geneInitializer = Objects.requireNonNull(geneInitializer);
        Objects.requireNonNull(random);
        RequireUtils.positive(minLength);
        RequireUtils.less(minLength, maxLength);
        lengthProvider = () -> random.nextInt(minLength, maxLength);
    }

    public GeneBasedInitializer(Supplier<G> geneInitializer, Supplier<Integer> lengthProvider) {
        this.geneInitializer = Objects.requireNonNull(geneInitializer);
        this.lengthProvider = Objects.requireNonNull(lengthProvider);
    }

    @Override
    public Stream<Chromosome<G>> produceChromosomes(int populationSize) {
        return Stream.generate(this)
                .limit(populationSize);
    }

    @Override
    public final Chromosome<G> get() {
        return new BaseChromosome<>(
                Stream.generate(geneInitializer)
                        .limit(lengthProvider.get())
                        .toList()
        );
    }
}
