package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.BinaryCrossover;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class UniformCrossover<G extends Gene<?, G>, F> extends BinaryCrossover<G, F> {
    private final RandomGenerator random;
    private final double exchangeProbability;

    public UniformCrossover(RandomGenerator random) {
        this(random, 0.5);
    }

    public UniformCrossover(RandomGenerator random, double exchangeProbability) {
        super(random);
        this.exchangeProbability = RequireUtils.probability(exchangeProbability);
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public List<Chromosome<G>> performCrossover(Chromosome<G> first, Chromosome<G> second) {
        int length = Math.min(first.length(), second.length());
        List<G> firstGenes = new ArrayList<>(first.getGenes());
        List<G> secondGenes = new ArrayList<>(second.getGenes());
        for (int i = 0; i < length; i++) {
            if (RandomUtils.spin(random, exchangeProbability)) {
                firstGenes.set(i, second.getGene(i));
                secondGenes.set(i, first.getGene(i));
            }
        }
        return List.of(first.newInstance(firstGenes), second.newInstance(secondGenes));
    }
}
