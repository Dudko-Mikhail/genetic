package by.dudko.genetic.stepProcess.simplegeneralization;

import by.dudko.genetic.model.chromosome.Chromosome;

import java.util.stream.Stream;

public interface PopulationInitializer<T> {
    Stream<Chromosome<T>> initializePopulation(int populationSize);
}
