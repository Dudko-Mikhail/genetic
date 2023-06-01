package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.util.IndexSelector;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;
import by.dudko.genetic.util.SelectorsFactory;

import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class ChromosomeBasedMutation<T> implements PopulationMutation<T> {
    private final IndexSelector indexSelector;
    private final UnaryOperator<Chromosome<T>> chromosomeMutator;

    public ChromosomeBasedMutation(RandomGenerator random, double chromosomeMutationProbability,
                                   UnaryOperator<Chromosome<T>> chromosomeMutator) {
        this(SelectorsFactory.probabilitySelector(random, chromosomeMutationProbability), chromosomeMutator);
    }

    public ChromosomeBasedMutation(IndexSelector indexSelector, UnaryOperator<Chromosome<T>> chromosomeMutator) {
        this.indexSelector = indexSelector;
        this.chromosomeMutator = chromosomeMutator;
    }

    @Override
    public final Population<T, ?> apply(Population<T, ?> population) {
        indexSelector.selectIndexes(population.getSize())
                .forEach(index -> {
                    Individual<T, ?> chromosome = population.getIndividual(index);
                    population.replaceIndividual(index, chromosomeMutator.apply(chromosome));
                });
        return population; // todo или создание новой популяции с удалением метода replaceChromosome?
    }
}
