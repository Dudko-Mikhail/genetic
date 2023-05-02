package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.GenericChromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.ChromosomeCrossover;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class UniformCrossover<T> implements ChromosomeCrossover<T> {
    private final RandomGenerator random;
    private final double exchangeProbability;

    public UniformCrossover(double exchangeProbability, RandomGenerator random) {
        this.exchangeProbability = RequireUtils.probability(exchangeProbability);
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public Chromosome<T> cross(Chromosome<T> first, Chromosome<T> second) {
        int length = Math.min(first.length(), second.length());
        List<Gene<T>> genes = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            Gene<T> currentGene = RandomUtils.roll(random, exchangeProbability) ? first.getGene(i) : second.getGene(i); // todo вынести блок с вероятностью (часто будет повторяться)
            genes.add(currentGene);
        }
        return new GenericChromosome<>(genes); // todo Завязка на конкретную реализацию
    }
}
