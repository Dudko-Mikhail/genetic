package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.Comparator;
import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public class TournamentSelection<G extends Gene<?, G>, F> implements Selection<G, F> {
    private final RandomGenerator random;
    private final Comparator<? super F> comparator;
    private final int roundSize;

    public TournamentSelection(RandomGenerator random, Comparator<? super F> comparator) {
        this(random, comparator, 2);
    }

    public TournamentSelection(RandomGenerator random, Comparator<? super F> comparator, int roundSize) {
        this.random = Objects.requireNonNull(random);
        this.comparator = Objects.requireNonNull(comparator);
        this.roundSize = RequireUtils.positive(roundSize);
    }

    @Override
    public Population<G, F> select(Population<G, F> population, int selectedPopulationSize) {
        RequireUtils.positive(selectedPopulationSize);
        return new Population<>(Stream.generate(() -> hostTournament(population))
                .limit(selectedPopulationSize)
                .toList(), population.getFitnessFunction());
    }

    private Individual<G, F> hostTournament(Population<G, F> population) {
        return RandomUtils.uniqueRandomIndexes(random, 0, population.getSize(), roundSize)
                .mapToObj(population::getIndividual)
                .reduce(this::determineWinner)
                .orElseThrow();
    }

    private Individual<G, F> determineWinner(Individual<G, F> first, Individual<G, F> second) {
        return comparator.compare(first.getFitness(), second.getFitness()) > 0 ? first : second;
    }
}
