package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.BinaryCrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class SinglePointCrossover<G extends Gene<?, G>, F> extends BinaryCrossover<G, F> {
    private final RandomGenerator random;

    public SinglePointCrossover(RandomGenerator random) {
        super(random);
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public List<Chromosome<G>> performCrossover(Chromosome<G> first, Chromosome<G> second) {
        int length = Math.min(first.length(), second.length()); // todo политика длины
        int pointPosition = random.nextInt(1, length - 1);
        List<G> firstGenes = new ArrayList<>();
        List<G> secondGenes = new ArrayList<>();
        for (int i = 0; i < pointPosition; i++) {
            firstGenes.add(first.getGene(i));
            secondGenes.add(second.getGene(i));
        }
        for (int i = pointPosition; i < second.length(); i++) {
            secondGenes.add(first.getGene(i));
            firstGenes.add(second.getGene(i));
        }
        return List.of(first.newInstance(firstGenes), second.newInstance(secondGenes));
    }
}
