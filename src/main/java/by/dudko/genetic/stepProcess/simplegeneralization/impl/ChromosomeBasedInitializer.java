package by.dudko.genetic.stepProcess.simplegeneralization.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.stepProcess.simplegeneralization.ChromosomeInitializer;
import by.dudko.genetic.stepProcess.simplegeneralization.PopulationInitializer;

import java.util.Objects;
import java.util.stream.Stream;

public class ChromosomeBasedInitializer<T> implements PopulationInitializer<T> {
    private final ChromosomeInitializer<T> chromosomeInitializer;

    public ChromosomeBasedInitializer(ChromosomeInitializer<T> chromosomeInitializer) {
        this.chromosomeInitializer = Objects.requireNonNull(chromosomeInitializer);
    }

    @Override
    public Stream<Chromosome<T>> initializePopulation(int populationSize) {
        return Stream.generate(chromosomeInitializer)
                .limit(populationSize);
    }
}