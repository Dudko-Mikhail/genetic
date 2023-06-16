package by.dudko.genetic.statistics;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

import java.util.Map;
import java.util.Optional;

public interface IStatistics<G extends Gene<?, G>, F> {
    <T> void registerMetric(String name, Metric<G, F, T> metric, Class<T> loggedType);

    Metric<G, F, ?> getMetric(String name);

    void calculateStatistics(Population<G, F> population, long generationNumber);

    <T> Optional<MetricLog<G, F, T>> fullMetricLog(String metric, Class<T> loggedType);

    <T> Optional<T> getMetricLogByGeneration(String metric, long generationNumber, Class<T> loggedType);

    Map<Long, GenerationStatistics<G, F>> getGenerationsStatistics();

    Optional<GenerationStatistics<G, F>> getGenerationStatistics(long generationNumber);

    void printMetricLog(String metric);

    void printMetricLogByGeneration(String metric, long generationNumber);

    void printGenerationStatistics(long generationNumber);

    void printFullStatistics();
}
