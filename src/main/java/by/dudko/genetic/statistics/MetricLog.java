package by.dudko.genetic.statistics;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class MetricLog<G extends Gene<?, G>, F, T> {
    private final String name;
    private final Metric<G, F, T> metric;
    private final Class<T> loggedType;
    private final Map<Long, T> generationLog = new LinkedHashMap<>();
    private final Function<? super T, String> formatter;

    public MetricLog(String name, Metric<G, F, T> metric, Class<T> loggedType) {
        this.name = Objects.requireNonNull(name);
        this.metric = Objects.requireNonNull(metric);
        this.loggedType = Objects.requireNonNull(loggedType);
        this.formatter = metric.formatter();
    }

    public void logGeneration(long generation, Population<G, F> population) {
        generationLog.put(generation, metric.apply(population));
    }

    public void printLog() {
        System.out.println("Metric name: " + name);
        generationLog.forEach((generation, value) -> {
            System.out.printf("generation %d - %s%n", generation, formatter.apply(value));
        });
    }

    public void printLogByGeneration(long generationNumber) {
        Optional.ofNullable(generationLog.get(generationNumber))
                .ifPresent(formatter::apply);
    }

    public Class<T> getLoggedType() {
        return loggedType;
    }

    public Metric<G, F, T> getMetric() {
        return metric;
    }

    public String getMetricName() {
        return name;
    }

    public Map<Long, T> getGenerationLog() {
        return generationLog;
    }

    public T getLogByGeneration(long generationNumber) {
        return generationLog.get(generationNumber);
    }

    public boolean isLoggedTypeValid(Class<?> loggedType) {
        if (loggedType == null) {
            return false;
        }
        return loggedType.isAssignableFrom(this.loggedType);
    }
}
