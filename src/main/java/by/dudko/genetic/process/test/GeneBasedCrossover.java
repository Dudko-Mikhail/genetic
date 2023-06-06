package by.dudko.genetic.process.test;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.function.Function;
import java.util.random.RandomGenerator;

public abstract class GeneBasedCrossover<G extends Gene<?, G>> extends ChromosomeBasedCrossover<G> {
    private Function<Chromosome<G>, G> selector;

    public GeneBasedCrossover(RandomGenerator random, int selectionSize) {
        super(random, selectionSize);
    }

    public GeneBasedCrossover(Selector<G> selector, int selectionSize) {
        super(selector, selectionSize);
    }

    @Override
    protected Chromosome<G> performCrossover(Chromosome<G>[] participants) {
        var first = participants[0];
        var second = participants[1];
        var gene1 = selector.apply(first);
        var gene2 = selector.apply(second);
        var resultGene = crossGenes(gene1, gene2);
        var firstGenes = first.getGenes();
        return null;
    }

    public abstract G crossGenes(G first, G second);
}
