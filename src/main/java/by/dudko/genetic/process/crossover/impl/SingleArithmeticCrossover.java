package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.BinaryCrossover;

import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class SingleArithmeticCrossover<G extends Gene<?, G>, F> extends BinaryCrossover<G, F> { // todo implement
    private final RandomGenerator random;
    private final double min;
    private final double max;

    public SingleArithmeticCrossover(RandomGenerator random, double min, double max) {
        super(random);
        this.random = Objects.requireNonNull(random);
        this.min = min;
        this.max = max;
    }

    @Override
    public List<Chromosome<G>> performCrossover(Chromosome<G> first, Chromosome<G> second) {
        int geneIndex = random.nextInt(Math.min(first.length(), second.length()));
        double coefficient = random.nextDouble(min, max);
        G firstGene = first.getGene(geneIndex);
        G secondGene = second.getGene(geneIndex);
        return null;
    }
}
