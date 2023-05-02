package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.process.evaluation.FitnessFunction;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.*;
import java.util.random.RandomGenerator;

public class TournamentSelection<T, F> implements Selection<T, F> { // todo validation
    private final RandomGenerator random;
    private final Comparator<? super F> comparator;
    private final int roundSize;

    public TournamentSelection(Comparator<? super F> comparator, RandomGenerator random) {
        this(comparator, 2, random);
    }

    public TournamentSelection(Comparator<? super F> comparator, int roundSize, RandomGenerator random) {
        this.random = Objects.requireNonNull(random);
        this.comparator = Objects.requireNonNull(comparator);
        this.roundSize = RequireUtils.positive(roundSize);
    }

    @Override
    public Population<T, F> select(Population<T, F> population, int selectedPopulationSize) {
        List<Chromosome<T>> selectedChromosomes = new ArrayList<>();
        while (selectedChromosomes.size() < selectedPopulationSize) {
            selectedChromosomes.add(hostTournament(population));
        }
        return new Population<>(selectedChromosomes, population.getFitnessFunction()); // todo конкретная реализация
    }

    private Chromosome<T> hostTournament(Population<T, F> population) {
        var chromosomes = population.getChromosomes();
        var fitnessFunction = population.getFitnessFunction();
        return RandomUtils.randomIndexes(random, 0, population.getSize(), roundSize)
                .mapToObj(chromosomes::get)
                .reduce((first, second) -> determineWinner(first, second, fitnessFunction))
                .orElseThrow();
    }

    private Chromosome<T> determineWinner(Chromosome<T> first, Chromosome<T> second,
                                          FitnessFunction<T, ? extends F> fitnessFunction) { // todo приспособленность уже вычеслена
        var firstFitness = fitnessFunction.apply(first);
        var secondFitness = fitnessFunction.apply(second);
        return comparator.compare(firstFitness, secondFitness) > 0 ? first : second;
    }
}
