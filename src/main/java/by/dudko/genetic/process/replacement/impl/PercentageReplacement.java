package by.dudko.genetic.process.replacement.impl;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class PercentageReplacement<T, F> implements Replacement<T, F> {
    private final double offspringPercentage;
    private final RandomGenerator random;

    public PercentageReplacement(RandomGenerator random, double offspringPercentage) {
        this.random = Objects.requireNonNull(random);
        this.offspringPercentage = RequireUtils.probability(offspringPercentage);
    }

    @Override
    public Population<T, F> replace(Population<T, F> oldGeneration, Population<T, F> offspring, int newPopulationSize) { // todo verify
        int oldGenerationSize = oldGeneration.getSize();
        int offSpringSize = offspring.getSize();
        int requiredOffspringSize = (int) Math.round(offspringPercentage * newPopulationSize);
        int requiredOldGenerationSize = newPopulationSize - requiredOffspringSize;
        RequireUtils.lessOrEqual(requiredOffspringSize, offSpringSize);
        RequireUtils.lessOrEqual(requiredOldGenerationSize, oldGenerationSize);

        List<Chromosome<T>> chromosomes = new ArrayList<>(newPopulationSize);
        RandomUtils.randomIndexes(random, 0, oldGenerationSize, requiredOldGenerationSize)
                .forEach(index -> chromosomes.add(oldGeneration.getChromosome(index)));
        RandomUtils.randomIndexes(random, 0, offSpringSize, requiredOffspringSize)
                .forEach(index -> chromosomes.add(offspring.getChromosome(index)));;
        return new Population<>(chromosomes, oldGeneration.getFitnessFunction());
    }
}
