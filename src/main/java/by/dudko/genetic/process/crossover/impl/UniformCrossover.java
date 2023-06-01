package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BaseGene;
import by.dudko.genetic.process.crossover.ChromosomeCrossover;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public class UniformCrossover<T> implements ChromosomeCrossover<T> {
    private final RandomGenerator random;
    private final double exchangeProbability;

    public UniformCrossover(RandomGenerator random) {
        this(random, 0.5);
    }

    public UniformCrossover(RandomGenerator random, double exchangeProbability) {
        this.exchangeProbability = RequireUtils.probability(exchangeProbability);
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public Chromosome<T> apply(Chromosome<T> first, Chromosome<T> second) {
        int length = Math.min(first.length(), second.length()); // fixme политика длины
        List<BaseGene<T>> genes = IntStream.range(0, length)
                .mapToObj(i -> RandomUtils.roll(random, exchangeProbability) ? first.getGene(i) : second.getGene(i))
                .toList();
        return first.newInstance(genes);
    }
}
