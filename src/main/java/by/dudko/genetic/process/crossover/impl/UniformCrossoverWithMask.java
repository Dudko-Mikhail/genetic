package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BaseGene;
import by.dudko.genetic.process.crossover.ChromosomeCrossover;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class UniformCrossoverWithMask<T> implements ChromosomeCrossover<T> {
    private final RandomGenerator random;
    private final double exchangeProbability;
    private final boolean[] mask;

    public UniformCrossoverWithMask(RandomGenerator random, double exchangeProbability, boolean[] mask) {
        this.random = Objects.requireNonNull(random);
        this.exchangeProbability = RequireUtils.probability(exchangeProbability);
        this.mask = Arrays.copyOf(mask, mask.length);
    }

    @Override
    public Chromosome<T> apply(Chromosome<T> first, Chromosome<T> second) {
        int length = Math.min(first.length(), second.length());
        RequireUtils.lessOrEqual(length, mask.length);
        List<BaseGene<T>> genes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (mask[i]) {
                genes.add(RandomUtils.roll(random, exchangeProbability) ? first.getGene(i) : second.getGene(i));
                continue;
            }
            genes.add(first.getGene(i));
        }
        return first.newInstance(genes);
    }
}
