package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.process.crossover.ChromosomeCrossover;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public class MultiPointCrossover<T> implements ChromosomeCrossover<T> {
    private final RandomGenerator random;
    private final int pointsNumber;

    public MultiPointCrossover(RandomGenerator random, int pointsNumber) {
        this.random = random;
        this.pointsNumber = RequireUtils.positive(pointsNumber);
    }

    @Override
    public Chromosome<T> cross(Chromosome<T> first, Chromosome<T> second) { // todo validation, finish algorithm
        int length = Math.min(first.length(), second.length());
        IntStream indexesStream = RandomUtils.randomIndexes(random, 0, length);
        int[] pointsIndexes = indexesStream.toArray();

        return null;
    }
}
