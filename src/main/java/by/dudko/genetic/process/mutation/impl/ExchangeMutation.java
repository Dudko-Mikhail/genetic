package by.dudko.genetic.process.mutation.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class ExchangeMutation<G extends Gene<?, G>> implements UnaryOperator<Chromosome<G>> {
    private final RandomGenerator random;

    public ExchangeMutation(RandomGenerator random) {
        this.random = random;
    }

    @Override
    public Chromosome<G> apply(Chromosome<G> chromosome) {
        int length = chromosome.length();
        var genes = new ArrayList<>(chromosome.getGenes());
        int[] swapIndexes = RandomUtils.uniqueRandomNumbers(random, 0, length, 2)
                .toArray();
        Collections.swap(genes, swapIndexes[0], swapIndexes[1]);
        return chromosome.newInstance(genes);
    }
}
