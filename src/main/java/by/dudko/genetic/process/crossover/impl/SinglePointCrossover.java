package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.ChromosomeCrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class SinglePointCrossover<G extends Gene<?, G>> implements ChromosomeCrossover<G> {
    private final RandomGenerator random;

    public SinglePointCrossover(RandomGenerator random) {
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public Chromosome<G> apply(Chromosome<G> first, Chromosome<G> second) {
        int length = Math.min(first.length(), second.length()); // todo политика длины
        int pointPosition = random.nextInt(1, length);
        List<G> genes = new ArrayList<>();
        for (int i = 0; i < pointPosition; i++) {
            genes.add(first.getGene(i));
        }
        for (int i = pointPosition; i < second.length(); i++) {
            genes.add(second.getGene(i));
        }
        return first.newInstance(genes);
    }
}
