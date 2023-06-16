package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.IndexSelector;
import by.dudko.genetic.util.IndexSelectors;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public abstract class ChromosomeBasedCrossover<G extends Gene<?, G>, F> implements PopulationCrossover<G, F> {
    private final IndexSelector<Individual<G, F>> individualSelector;

    protected ChromosomeBasedCrossover(RandomGenerator random, int selectionSize) {
        this(IndexSelectors.fixedSizeSelector(random, selectionSize));
    }

    protected ChromosomeBasedCrossover(IndexSelector<Individual<G, F>> chromosomeSelector) {
        this.individualSelector = Objects.requireNonNull(chromosomeSelector);
    }

    @Override
    public final Population<G, F> performCrossover(Population<G, F> population, int offspringSize) {
        var offspring = Stream.generate(() -> {
                    var chromosomes = individualSelector.select(population.getIndividuals())
                            .toList();
                    return performCrossover(chromosomes);
                })
                .flatMap(Collection::stream)
                .limit(offspringSize)
                .toList();
        return new Population<>(offspring, population.getFitnessFunction());
    }

    protected abstract List<Chromosome<G>> performCrossover(List<? extends Chromosome<G>> participants);
}
