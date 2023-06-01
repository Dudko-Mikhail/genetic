package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BaseGene;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.IndexSelector;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.SelectorsFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class GeneBasedMutation<T> implements UnaryOperator<Chromosome<T>> { // todo Наследование от ChromosomeBasedMutator
    private final IndexSelector indexSelector;
    private final UnaryOperator<BaseGene<T>> mutagen;

    public GeneBasedMutation(IndexSelector selector, UnaryOperator<BaseGene<T>> mutagen) {
        this.indexSelector = Objects.requireNonNull(selector);
        this.mutagen = Objects.requireNonNull(mutagen);
    }

    @Override
    public final Chromosome<T> apply(Chromosome<T> chromosome) { // todo Избавиться от метода replaceGene; Можно вынести интерфейс селектор, отвечающий за выборку индексов
        indexSelector.selectIndexes(chromosome.length())
                .forEach(index -> {
                    BaseGene<T> gene = chromosome.getGene(index);
                    chromosome.replaceGene(index, mutagen.apply(gene));
                });
        return chromosome;
    }
}


abstract class InheritanceChromosomeBasedMutation<T> implements PopulationMutation<T> {
    private final IndexSelector chromosomeSelector;

    protected InheritanceChromosomeBasedMutation(RandomGenerator random, double chromosomeMutationProbability) {
        this(SelectorsFactory.probabilitySelector(random, chromosomeMutationProbability));
    }

    protected InheritanceChromosomeBasedMutation(IndexSelector chromosomeSelector) {
        this.chromosomeSelector = chromosomeSelector;
    }

    @Override
    public final Population<T, ?> apply(Population<T, ?> population) {
        chromosomeSelector.selectIndexes(population.getSize())
                .forEach(index -> {
                    Individual<T, ?> chromosome = population.getIndividual(index);
                    population.replaceIndividual(index, mutateChromosome(chromosome));
                });
        return population;
    }

    protected abstract Chromosome<T> mutateChromosome(Chromosome<T> chromosome);
}

abstract class InheritanceGeneBasedMutation<T> extends InheritanceChromosomeBasedMutation<T> {
    private final IndexSelector geneSelector;

    public InheritanceGeneBasedMutation(IndexSelector chromosomeSelector, IndexSelector geneSelector) {
        super(chromosomeSelector);
        this.geneSelector = Objects.requireNonNull(geneSelector);
    }

    @Override
    protected final Chromosome<T> mutateChromosome(Chromosome<T> chromosome) {
        geneSelector.selectIndexes(chromosome.length())
                .forEach(index -> {
                    Gene<T> gene = chromosome.getGene(index);
                    chromosome.replaceGene(index, mutateGene(gene));
                });
        return chromosome;
    }

    protected abstract BaseGene<T> mutateGene(Gene<T> gene);
}


class ExchangeMutation<T> extends InheritanceChromosomeBasedMutation<T> {
    private final RandomGenerator random;
    public ExchangeMutation(IndexSelector chromosomeSelector, RandomGenerator random) {
        super(chromosomeSelector);
        this.random = random;
    }

    @Override
    public Chromosome<T> mutateChromosome(Chromosome<T> chromosome) {
        int length = chromosome.length();
        int[] swapIndexes = RandomUtils.uniqueRandomIndexes(random, 0, length, 2)
                .toArray();
        var oldGene = chromosome.replaceGene(swapIndexes[0], chromosome.getGene(swapIndexes[1]));
        chromosome.replaceGene(swapIndexes[1], oldGene);
        return chromosome;
    }
}



