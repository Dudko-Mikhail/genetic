package by.dudko.genetic.statistics;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class GenerationStatistics<G extends Gene<?, G>, F> {
    private static final String SEPARATOR = "=".repeat(40) + "\n";
    private final long generationNumber;
    private final Map<String, String> metricsLog = new HashMap<>();

    public GenerationStatistics(long generationNumber, Population<G, F> population,
                                Map<String, Metric<G, F, ?>> metrics) {
        this.generationNumber = generationNumber;
        metrics.forEach((name, metric) -> metricsLog.put(name, metric.stringify(population)));
    }

    public void printStatistics() {
        System.out.println(this);
    }

    public String getMetricLog(String metric) {
        return metricsLog.get(metric);
    }

    public Map<String, String> getMetricsLog() {
        return metricsLog;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n", SEPARATOR, SEPARATOR);
        joiner.add("Generation number: " + generationNumber);
        metricsLog.forEach((name, metric) -> {
            joiner.add("%s - %s".formatted(name, metric));
        });
        joiner.add("");
        return joiner.toString();
    }
}
