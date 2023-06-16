package by.dudko.genetic.process.mutation.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class ShuffleMutationWith2Points<G extends Gene<?, G>> implements UnaryOperator<Chromosome<G>> {
    private final RandomGenerator random;

    public ShuffleMutationWith2Points(RandomGenerator random) {
        this.random = random;
    }

    @Override
    public Chromosome<G> apply(Chromosome<G> chromosome) {
        return null;
    }
}
