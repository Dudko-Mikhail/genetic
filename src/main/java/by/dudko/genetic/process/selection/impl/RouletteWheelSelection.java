package by.dudko.genetic.process.selection.impl;

import by.dudko.genetic.model.Population;

import java.util.Objects;
import java.util.function.Function;
import java.util.random.RandomGenerator;

public class RouletteWheelSelection<T, F> extends ProbabilitySelection<T, F> {
    private final Function<F, Double> mapper;

    public RouletteWheelSelection(RandomGenerator random, Function<F, Double> mapper) {
        super(random);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public double[] calculateProbabilities(Population<T, F> population) {
        double sum = 0;
        int populationSize = population.getSize();
        double[] dataArray = new double[populationSize];
        for (int i = 0; i < populationSize; i++) {
            var individual = population.getIndividual(i);
            dataArray[i] = mapper.apply(individual.getFitness());
            sum += dataArray[i];
        }
        for (int i = 0; i < populationSize; i++) {
            dataArray[i] = dataArray[i] / sum;
        }
        return dataArray;
    }
}