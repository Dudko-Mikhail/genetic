package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.random.RandomGenerator;

public class EliteSelection<T, F> implements Selection<T, F> { // todo прогуглить определение. Нужно узнать в какой селекции отбираются особи со значением приспособленности выше среднего
    private final double elitePercentage;
    private final Selection<T, F> remainingSelector;
    private final Comparator<? super  F> comparator;

    public EliteSelection(RandomGenerator random, double elitePercentage, Comparator<? super  F> comparator) {
        this(new MonteCarloSelection<>(random), elitePercentage, comparator);
    }

    public EliteSelection(Selection<T, F> remainingSelector, double elitePercentage, Comparator<? super  F> comparator) {
        this.elitePercentage = RequireUtils.probability(elitePercentage);
        this.remainingSelector = remainingSelector;
        this.comparator = comparator;
    }

    @Override
    public Population<T, F> select(Population<T, F> population, int selectedPopulationSize) {
        RequireUtils.positive(selectedPopulationSize);
        int eliteCount = (int) (selectedPopulationSize * elitePercentage);
        int remaining = Math.max(selectedPopulationSize - eliteCount, 0);
        population.sort(comparator.reversed());
        List<Individual<T, F>> eliteIndividuals = population.getIndividuals()
                .stream()
                .limit(eliteCount)
                .toList();
        var remainingIndividuals = new ArrayList<>(remainingSelector.select(population, remaining).getIndividuals());
        remainingIndividuals.addAll(eliteIndividuals);
        return new Population<>(remainingIndividuals, population.getFitnessFunction());
    }
}
