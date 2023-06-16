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

public class MultiPointCrossover<G extends Gene<?, G>, F> extends BinaryCrossover<G, F> { // todo Проверить валидацию
    private final RandomGenerator random;
    private final int pointsNumber;

    public MultiPointCrossover(RandomGenerator random, int pointsNumber) {
        super(random);
        this.random = Objects.requireNonNull(random);
        this.pointsNumber = RequireUtils.positive(pointsNumber);
    }

    @Override
    public List<Chromosome<G>> performCrossover(Chromosome<G> first, Chromosome<G> second) {
        int length = Math.min(first.length(), second.length()); // todo политика длины
        int pointsNumber = Math.min(this.pointsNumber, length - 1);
        int[] indexes = RandomUtils.uniqueRandomNumbers(random, 1, length - 1, pointsNumber)
                .sorted()
                .toArray();
        List<G> firstGenes = new ArrayList<>(length);
        List<G> secondGenes = new ArrayList<>(length);
        fillChromosome(0, 0, indexes, first, second, firstGenes, secondGenes);
        return List.of(first.newInstance(firstGenes), second.newInstance(secondGenes));
    }

    private void fillChromosome(int start, int position, int[] indexes, Chromosome<G> first, Chromosome<G> second,
                                List<G> firstGenes, List<G> secondGenes) {
        if (position > indexes.length) {
            return;
        }
        int end = position < indexes.length ? indexes[position] : Math.min(first.length(), second.length());
        for (int i = start; i < end; i++) {
            firstGenes.add(first.getGene(i));
            secondGenes.add(second.getGene(i));
        }
        fillChromosome(end, position + 1, indexes, second, first, firstGenes, secondGenes);
    }
}
