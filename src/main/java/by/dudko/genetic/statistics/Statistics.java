package by.dudko.genetic.statistics;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.RequireUtils;

import java.util.*;
import java.util.function.Consumer;

public class Statistics<G extends Gene<?, G>, F> implements IStatistics<G, F> {
    private final int logPeriod;
    private final TimeStatistics timeStatistics = new TimeStatistics();
    private final Map<String, Metric<G, F, ?>> metrics = new HashMap<>();
    private final Map<Long, GenerationStatistics<G, F>> generationsLog = new LinkedHashMap<>();
    private final Map<String, MetricLog<G, F, ?>> metricsLog = new LinkedHashMap<>();
    private final Consumer<GenerationStatistics<G, F>> printAction;

    public static <G extends Gene<?, G>, F> Statistics<G, F> statisticsWithoutConsoleLog(int logPeriod) {
        return new Statistics<>(logPeriod, (log) -> {
        });
    }

    public Statistics(int logPeriod) {
        this(logPeriod, GenerationStatistics::printStatistics);
    }

    public Statistics(int logPeriod, Consumer<GenerationStatistics<G, F>> printAction) {
        this.logPeriod = RequireUtils.positive(logPeriod);
        this.printAction = Objects.requireNonNull(printAction);
    }

    @Override
    public <T> void registerMetric(String name, Metric<G, F, T> metric, Class<T> loggedType) {
        metrics.put(name, metric);
        metricsLog.put(name, new MetricLog<>(name, metric, loggedType));
    }

    @Override
    public Metric<G, F, ?> getMetric(String name) {
        return metrics.get(name);
    }

    @Override
    public void calculateStatistics(Population<G, F> population, long generationNumber) {
        if (generationNumber % logPeriod == 0) {
            metrics.forEach((name, metric) -> {
                var metricLog = metricsLog.get(name);
                metricLog.logGeneration(generationNumber, population);
            });
            GenerationStatistics<G, F> log = new GenerationStatistics<>(generationNumber, population, metrics);
            generationsLog.put(generationNumber, log);
            printAction.accept(log);
        }
    }

    @Override
    public Map<Long, GenerationStatistics<G, F>> getGenerationsStatistics() {
        return Collections.unmodifiableMap(generationsLog);
    }

    public TimeStatistics getTimeStatistics() {
        return timeStatistics;
    }

    @Override
    public <T> Optional<MetricLog<G, F, T>> fullMetricLog(String metric, Class<T> loggedType) {
        var metricLog = metricsLog.get(metric);
        if (metricLog == null) {
            Optional.empty();
        }
        if (!metricLog.isLoggedTypeValid(loggedType)) {
            throw new IllegalArgumentException("Given logged type [%s] is not assignable from [%s]"
                    .formatted(loggedType, metricLog.getLoggedType()));
        }
        return Optional.of((MetricLog<G, F, T>) metricLog);
    }

    @Override
    public <T> Optional<T> getMetricLogByGeneration(String metric, long generationNumber, Class<T> loggedType) {
        return Optional.ofNullable(metricsLog.get(metric))
                .map(metricLog -> {
                    if (!metricLog.isLoggedTypeValid(loggedType)) {
                        return null;
                    }
                    return loggedType.cast(metricLog.getLogByGeneration(generationNumber));
                });
    }

    @Override
    public Optional<GenerationStatistics<G, F>> getGenerationStatistics(long generationNumber) {
        return Optional.ofNullable(generationsLog.get(generationNumber));
    }

    @Override
    public void printMetricLogByGeneration(String metric, long generationNumber) {
        Optional.ofNullable(metricsLog.get(metric))
                .ifPresent(metricLog -> metricLog.printLogByGeneration(generationNumber));
    }

    @Override
    public void printGenerationStatistics(long generationNumber) {
        generationsLog.get(generationNumber).printStatistics();
    }

    public void printTimeStatistics() {
        timeStatistics.printStatistics();
    }

    @Override
    public void printFullStatistics() {
        timeStatistics.printStatistics();
        generationsLog.values()
                .forEach(GenerationStatistics::printStatistics);
    }

    @Override
    public void printMetricLog(String metric) {
        Optional.ofNullable(metricsLog.get(metric))
                .ifPresent(MetricLog::printLog);
    }
}