package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BaseGene;
import by.dudko.genetic.process.crossover.ChromosomeCrossover;

import java.util.Objects;
import java.util.random.RandomGenerator;

public class SingleArithmeticCrossover<T> implements ChromosomeCrossover<T> { // todo implement
    private final RandomGenerator random;
    private final double min;
    private final double max;

    public SingleArithmeticCrossover(RandomGenerator random, double min, double max) {
        this.random = Objects.requireNonNull(random);
        this.min = min;
        this.max = max;
    }

    @Override
    public Chromosome<T> apply(Chromosome<T> first, Chromosome<T> second) {
        int geneIndex = random.nextInt(Math.min(first.length(), second.length()));
        double coefficient = random.nextDouble(min, max);
        BaseGene<T> firstGene = first.getGene(geneIndex);
        BaseGene<T> secondGene = second.getGene(geneIndex);
        return null;
    }
}
