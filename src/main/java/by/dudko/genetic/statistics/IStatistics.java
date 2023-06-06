package by.dudko.genetic.statistics;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

import java.util.List;
import java.util.Optional;

public interface IStatistics <G extends Gene<?, G>, F> {
    <T> List<T> fullMetricLog(String metric, Class<T> loggedType);

    <T> Optional<T> getLog(String metric, long generationNumber, Class<T> loggedType);

    <T> List<T> generationLog(long generationNumber, Class<T> loggedType);

    Metric<G, F, ?> getMetric(String name);

    void calculateStatistics(Population<G, F> population, long generationNumber);

    void registerMetric(Metric<G, F, ?> metric);

    void printMetricLog(String metric);

    void printMetricLogByGeneration(String metric, long generationNumber);

    void printGenerationLog(long generationNumber);

    void printFullStatistics();
}
