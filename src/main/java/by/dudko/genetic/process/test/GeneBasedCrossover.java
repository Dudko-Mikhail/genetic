package by.dudko.genetic.process.test;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.function.Function;
import java.util.random.RandomGenerator;

public abstract class GeneBasedCrossover<T> extends ChromosomeBasedCrossover<T> {
    private Function<Chromosome<T>, Gene<T>> selector;

    public GeneBasedCrossover(RandomGenerator random, int selectionSize) {
        super(random, selectionSize);
    }

    public GeneBasedCrossover(Selector<T> selector, int selectionSize) {
        super(selector, selectionSize);
    }

    @Override
    protected Chromosome<T> performCrossover(Chromosome<T>[] participants) {
        var first = participants[0];
        var second = participants[1];
        var gene1 = selector.apply(first);
        var gene2 = selector.apply(second);
        var resultGene = crossGenes(gene1, gene2);
        var firstGenes = first.getGenes();
        return null;
    }

    public abstract Gene<T> crossGenes(Gene<T> first, Gene<T> second);
}
