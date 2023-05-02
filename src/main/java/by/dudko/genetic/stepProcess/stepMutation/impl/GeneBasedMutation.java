package by.dudko.genetic.stepProcess.stepMutation.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.stepProcess.stepMutation.PopulationMutation;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class GeneBasedMutation<T> implements UnaryOperator<Chromosome<T>> { // todo генетический мутатор как параметер метода apply; Наследование от ChromosomeBasedMutatorв
    protected final double geneMutationProbability;
    private final RandomGenerator random;
    private final UnaryOperator<Gene<T>> mutagen;

    public GeneBasedMutation(RandomGenerator random, double geneMutationProbability, UnaryOperator<Gene<T>> mutagen) {
        this.random = Objects.requireNonNull(random);
        this.geneMutationProbability = RequireUtils.probability(geneMutationProbability);
        this.mutagen = Objects.requireNonNull(mutagen);
    }

    public PopulationMutation<T> toPopulationMutation(double chromosomeMutationProbability) {
        return toPopulationMutation(random, chromosomeMutationProbability);
    }

    public PopulationMutation<T> toPopulationMutation(RandomGenerator random, double chromosomeMutationProbability) {
        return new ChromosomeBasedMutation<>(random, chromosomeMutationProbability, this);
    }

    @Override
    public final Chromosome<T> apply(Chromosome<T> chromosome) { // todo Избавиться от метода replaceGene; Можно вынести интерфейс селектор, отвечающий за выборку индексов
        RandomUtils.randomIndexes(random, 0, chromosome.length(), geneMutationProbability)
                .forEach(index -> {
                    Gene<T> gene = chromosome.getGene(index);
                    chromosome.replaceGene(index, mutagen.apply(gene));
                });
        return chromosome;
    }
}