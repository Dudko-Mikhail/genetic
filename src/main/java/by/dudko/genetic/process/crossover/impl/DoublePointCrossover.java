package by.dudko.genetic.process.crossover.impl;

import java.util.random.RandomGenerator;

public class DoublePointCrossover<T> extends MultiPointCrossover<T> {
    public DoublePointCrossover(RandomGenerator random) {
        super(random, 2);
    }
}
