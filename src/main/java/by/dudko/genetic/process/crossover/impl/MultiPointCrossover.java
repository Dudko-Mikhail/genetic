package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BaseGene;
import by.dudko.genetic.process.crossover.ChromosomeCrossover;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class MultiPointCrossover<T> implements ChromosomeCrossover<T> { // todo Проверить валидацию
    private final RandomGenerator random;
    private final int pointsNumber;

    public MultiPointCrossover(RandomGenerator random, int pointsNumber) {
        this.random = Objects.requireNonNull(random);
        this.pointsNumber = RequireUtils.positive(pointsNumber);
    }

    @Override
    public Chromosome<T> apply(Chromosome<T> first, Chromosome<T> second) {
        int length = Math.min(first.length(), second.length());
        RequireUtils.less(pointsNumber, length);
        int[] indexes = RandomUtils.uniqueRandomIndexes(random, 1, length, pointsNumber)
                .sorted()
                .toArray();
        List<BaseGene<T>> genes = new ArrayList<>();
        fillChromosome(0, 0, indexes, first, second, genes);
        return first.newInstance(genes);
    }

    private void fillChromosome(int start, int position, int[] indexes,
                                Chromosome<T> first, Chromosome<T> second, List<BaseGene<T>> genes) {
        if (position > indexes.length) {
            return;
        }
        int end = position < indexes.length ? indexes[position] : first.length();
        for (int i = start; i < end; i++) {
            genes.add(first.getGene(i));
        }
        fillChromosome(end, position + 1, indexes, second, first, genes);
    }
}
