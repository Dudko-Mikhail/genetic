package by.dudko.genetic.stepProcess.stepMutation.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.stepProcess.stepMutation.PopulationMutation;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class ChromosomeBasedMutation<T> implements PopulationMutation<T> { // todo хромосомный мутатор как параметер метода apply.
    protected final RandomGenerator random;
    private final UnaryOperator<Chromosome<T>> chromosomeMutator;
    private final double chromosomeMutationProbability;

    public ChromosomeBasedMutation(RandomGenerator random, double chromosomeMutationProbability,
                                   UnaryOperator<Chromosome<T>> chromosomeMutator) {
        this.chromosomeMutationProbability = RequireUtils.probability(chromosomeMutationProbability);
        this.random = Objects.requireNonNull(random);
        this.chromosomeMutator = Objects.requireNonNull(chromosomeMutator);
    }

    @Override
    public final List<Chromosome<T>> apply(List<Chromosome<T>> chromosomes) {
        RandomUtils.randomIndexes(random, 0, chromosomes.size(), chromosomeMutationProbability)
                .forEach(index -> {
                    Chromosome<T> chromosome = chromosomes.get(index);
                    chromosomes.set(index, chromosomeMutator.apply(chromosome));
                });
        return chromosomes;
    }
}
