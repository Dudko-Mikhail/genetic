package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.gene.Gene;

import java.util.random.RandomGenerator;

public class DoublePointCrossover<G extends Gene<?, G>, F> extends MultiPointCrossover<G, F> {
    public DoublePointCrossover(RandomGenerator random) {
        super(random, 2);
    }
}
