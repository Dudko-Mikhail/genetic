package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.IndexSelector;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.SelectorsFactory;

import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class GeneBasedMutation<G extends Gene<?, G>> implements UnaryOperator<Chromosome<G>> { // todo Наследование от ChromosomeBasedMutator
    private final IndexSelector indexSelector;
    private final UnaryOperator<G> mutagen;

    public GeneBasedMutation(IndexSelector selector, UnaryOperator<G> mutagen) {
        this.indexSelector = Objects.requireNonNull(selector);
        this.mutagen = Objects.requireNonNull(mutagen);
    }

    @Override
    public final Chromosome<G> apply(Chromosome<G> chromosome) { // todo Избавиться от метода replaceGene; Можно вынести интерфейс селектор, отвечающий за выборку индексов
        indexSelector.selectIndexes(chromosome.length())
                .forEach(index -> {
                    G gene = chromosome.getGene(index);
                    chromosome.replaceGene(index, mutagen.apply(gene));
                });
        return chromosome;
    }
}


abstract class InheritanceChromosomeBasedMutation<G extends Gene<?, G>> implements PopulationMutation<G> {
    private final IndexSelector chromosomeSelector;

    protected InheritanceChromosomeBasedMutation(RandomGenerator random, double chromosomeMutationProbability) {
        this(SelectorsFactory.probabilitySelector(random, chromosomeMutationProbability));
    }

    protected InheritanceChromosomeBasedMutation(IndexSelector chromosomeSelector) {
        this.chromosomeSelector = chromosomeSelector;
    }

    @Override
    public final Population<G, ?> apply(Population<G, ?> population) {
        chromosomeSelector.selectIndexes(population.getSize())
                .forEach(index -> {
                    Individual<G, ?> chromosome = population.getIndividual(index);
                    population.replaceIndividual(index, mutateChromosome(chromosome));
                });
        return population;
    }

    protected abstract Chromosome<G> mutateChromosome(Chromosome<G> chromosome);
}

abstract class InheritanceGeneBasedMutation<G extends Gene<?, G>> extends InheritanceChromosomeBasedMutation<G> {
    private final IndexSelector geneSelector;

    public InheritanceGeneBasedMutation(IndexSelector chromosomeSelector, IndexSelector geneSelector) {
        super(chromosomeSelector);
        this.geneSelector = Objects.requireNonNull(geneSelector);
    }

    @Override
    protected final Chromosome<G> mutateChromosome(Chromosome<G> chromosome) {
        geneSelector.selectIndexes(chromosome.length())
                .forEach(index -> {
                    G gene = chromosome.getGene(index);
                    chromosome.replaceGene(index, mutateGene(gene));
                });
        return chromosome;
    }

    protected abstract G mutateGene(G gene);
}


class ExchangeMutation<G extends Gene<?, G>> extends InheritanceChromosomeBasedMutation<G> {
    private final RandomGenerator random;

    public ExchangeMutation(IndexSelector chromosomeSelector, RandomGenerator random) {
        super(chromosomeSelector);
        this.random = random;
    }

    @Override
    public Chromosome<G> mutateChromosome(Chromosome<G> chromosome) {
        int length = chromosome.length();
        int[] swapIndexes = RandomUtils.uniqueRandomIndexes(random, 0, length, 2)
                .toArray();
        var oldGene = chromosome.replaceGene(swapIndexes[0], chromosome.getGene(swapIndexes[1]));
        chromosome.replaceGene(swapIndexes[1], oldGene);
        return chromosome;
    }
}



