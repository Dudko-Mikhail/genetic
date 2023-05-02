package by.dudko.genetic.process.mutation.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.GenericChromosome;
import by.dudko.genetic.process.mutation.ChromosomeMutation;
import by.dudko.genetic.util.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.random.RandomGenerator;

public class ExchangeMutation<T> implements ChromosomeMutation<T> {
    private final RandomGenerator random;

    public ExchangeMutation(RandomGenerator random) {
        this.random = random;
    }

    @Override
    public Chromosome<T> mutateChromosome(Chromosome<T> chromosome) {
        int length = chromosome.length();
        var genes = new ArrayList<>(chromosome.getGenes());
        int[] swapIndexes = RandomUtils.randomIndexes(random, 0, length, 2)
                .toArray();
        Collections.swap(genes, swapIndexes[0], swapIndexes[1]);
        return new GenericChromosome<>(genes); // todo Завязка на конкретную реализацию
    }
}
