package by.dudko.genetic.statistics;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class StatisticsLog<G extends Gene<?, G>, F> {
    private static final String SEPARATOR = "=".repeat(40);

    private final long generationNumber;
    private final Map<String, String> metricsResults = new HashMap<>();

    public StatisticsLog(long generationNumber, Population<G, F> population, Map<String, Metric<G, F, ?>> map) {
        this.generationNumber = generationNumber;
        map.forEach((name, metric) -> metricsResults.put(name, metric.stringify(population)));
    }

    public void printStatistics() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(SEPARATOR);
        joiner.add("Generation number: " + generationNumber);
        metricsResults.forEach((name, metric) -> {
            joiner.add("%s - %s".formatted(name, metric));
        });
        joiner.add(SEPARATOR);
        joiner.add("");
        return joiner.toString();
    }
}
