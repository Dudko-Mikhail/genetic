package by.dudko.genetic.statistics;

import java.util.*;
import java.util.concurrent.Callable;

public class TimeStatistics {
    private final Map<EvolutionProcess, LongSummaryStatistics> timeStatistics = new EnumMap<>(EvolutionProcess.class);

    public TimeStatistics() {
        Arrays.stream(EvolutionProcess.values())
                .forEach(process -> timeStatistics.put(process, new LongSummaryStatistics()));
    }

    public LongSummaryStatistics getTimeStatistics(EvolutionProcess evolutionProcess) {
        return timeStatistics.get(evolutionProcess);
    }

    public void acceptTime(EvolutionProcess evolutionProcess, long durationInMillis) {
        LongSummaryStatistics statistics = timeStatistics.get(evolutionProcess);
        statistics.accept(durationInMillis);
    }

    public Set<Map.Entry<EvolutionProcess, LongSummaryStatistics>> entrySet() {
        return timeStatistics.entrySet();
    }

    public <V> V measureTime(EvolutionProcess evolutionProcess, Callable<V> action) {
        long start = System.currentTimeMillis();
        try {
            V result = action.call();
            acceptTime(evolutionProcess, System.currentTimeMillis() - start);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    public void measureTime(EvolutionProcess evolutionProcess, Runnable action) {
        long start = System.currentTimeMillis();
        action.run();
        acceptTime(evolutionProcess, System.currentTimeMillis() - start);
    }

    public void printStatistics() {
        System.out.printf("%1$s Time Statistics %1$s%n", "=".repeat(20));
        timeStatistics.forEach((process, statistics) -> System.out.printf("%s - %s%n", process, statistics));
    }

    public enum EvolutionProcess {
        SELECTION,
        MUTATION,
        EVALUATION,
        REPLACEMENT,
        CROSSOVER
    }
}
