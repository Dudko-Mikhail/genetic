package by.dudko.genetic.algorithm.listeners.impl;

import by.dudko.genetic.algorithm.GeneticAlgorithm;
import by.dudko.genetic.algorithm.listeners.EvolutionInterceptor;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.statistics.TimeStatistics;

import static by.dudko.genetic.statistics.TimeStatistics.MeasuredProcesses.*;

public class TimeStatisticsEvolutionInterceptor<G extends Gene<?, G>, F> implements EvolutionInterceptor<G, F> { // todo remove
    private final TimeStatistics timeStatistics;
    private long start;

    public TimeStatisticsEvolutionInterceptor(TimeStatistics timeStatistics) {
        this.timeStatistics = timeStatistics;
    }

    @Override
    public void beforeSelection(GeneticAlgorithm<G, F> geneticAlgorithm) {
        long generation = geneticAlgorithm.getGenerationNumber();
        if (generation != 1) {
            timeStatistics.acceptTime(REPLACEMENT, timeStatistics.currentMillis() - start);
        }
        start = timeStatistics.currentMillis();
    }

    @Override
    public void beforeCrossover(GeneticAlgorithm<G, F> geneticAlgorithm, Population<G, F> parents) {
        timeStatistics.acceptTime(SELECTION, timeStatistics.currentMillis() - start);
        start = timeStatistics.currentMillis();
    }

    @Override
    public void beforeMutation(GeneticAlgorithm<G, F> geneticAlgorithm, Population<G, F> population) {
        timeStatistics.acceptTime(CROSSOVER, timeStatistics.currentMillis() - start);
        start = timeStatistics.currentMillis();
    }

    @Override
    public void beforeReplacement(GeneticAlgorithm<G, F> geneticAlgorithm, Population<G, F> mutatedOffspring) {
        timeStatistics.acceptTime(MUTATION, timeStatistics.currentMillis() - start);
        start = timeStatistics.currentMillis();
    }
}
