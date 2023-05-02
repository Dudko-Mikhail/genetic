package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.GenericChromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.ChromosomeCrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;

public class SinglePointCrossover<T> implements ChromosomeCrossover<T> {
    private final RandomGenerator random;

    public SinglePointCrossover() {
        this(new Random());
    }

    public SinglePointCrossover(RandomGenerator random) {
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public Chromosome<T> cross(Chromosome<T> first, Chromosome<T> second) {
        int length = Math.min(first.length(), second.length());
        int pointPosition = random.nextInt(length);
        List<Gene<T>> genes = new ArrayList<>();
        for (int i = 0; i < pointPosition; i++) {
            genes.add(first.getGene(i));
        }
        for (int i = pointPosition; i < second.length(); i++) {
            genes.add(second.getGene(i));
        }
        return new GenericChromosome<>(genes); // todo Завязка на конкретную реализацию
    }
}
