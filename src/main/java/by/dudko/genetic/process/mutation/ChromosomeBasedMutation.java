package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.IndexSelector;
import by.dudko.genetic.util.SelectorsFactory;

import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class ChromosomeBasedMutation<G extends Gene<?, G>> implements PopulationMutation<G> {
    private final IndexSelector indexSelector;
    private final UnaryOperator<Chromosome<G>> chromosomeMutator;

    public ChromosomeBasedMutation(RandomGenerator random, double chromosomeMutationProbability,
                                   UnaryOperator<Chromosome<G>> chromosomeMutator) {
        this(SelectorsFactory.probabilitySelector(random, chromosomeMutationProbability), chromosomeMutator);
    }

    public ChromosomeBasedMutation(IndexSelector indexSelector, UnaryOperator<Chromosome<G>> chromosomeMutator) {
        this.indexSelector = indexSelector;
        this.chromosomeMutator = chromosomeMutator;
    }

    @Override
    public final Population<G, ?> apply(Population<G, ?> population) {
        indexSelector.selectIndexes(population.getSize())
                .forEach(index -> {
                    Individual<G, ?> chromosome = population.getIndividual(index);
                    population.replaceIndividual(index, chromosomeMutator.apply(chromosome));
                });
        return population; // todo или создание новой популяции с удалением метода replaceChromosome?
    }
}
