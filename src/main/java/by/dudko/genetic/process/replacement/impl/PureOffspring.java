package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.random.RandomGenerator;

public class PureOffspring<T, F> implements Replacement<T, F> {
    private final RandomGenerator random;

    public PureOffspring(RandomGenerator random) {
        this.random = random;
    }

    @Override
    public Population<T, F> replace(Population<T, F> oldGeneration, Population<T, F> offspring, int newPopulationSize) {
        int offspringSize = offspring.getSize();
        RequireUtils.less(newPopulationSize, offspringSize);
        var chromosomes = RandomUtils.randomIndexes(random, 0, offspringSize, newPopulationSize)
                .mapToObj(offspring::getChromosome)
                .toList();
        return new Population<>(chromosomes, offspring.getFitnessFunction());
    }
}
