package by.dudko.genetic.statistics;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.Callable;

public class TimeStatistics {
    private static final Clock clock = Clock.systemUTC();
    private final Map<MeasuredProcesses, LongSummaryStatistics> timeStatistics = new EnumMap<>(MeasuredProcesses.class);

    public TimeStatistics() {
        Arrays.stream(MeasuredProcesses.values())
                .forEach(process -> timeStatistics.put(process, new LongSummaryStatistics()));
    }

    public LongSummaryStatistics getTimeStatistics(MeasuredProcesses measuredProcesses) {
        return timeStatistics.get(measuredProcesses);
    }

    public synchronized void acceptTime(MeasuredProcesses measuredProcesses, long durationInMillis) {
        LongSummaryStatistics statistics = timeStatistics.get(measuredProcesses);
        statistics.accept(durationInMillis);
    }

    public Set<Map.Entry<MeasuredProcesses, LongSummaryStatistics>> entrySet() {
        return timeStatistics.entrySet();
    }

    public synchronized  <V> V measureTime(MeasuredProcesses measuredProcesses, Callable<V> action) {
        long start = clock.millis();
        try {
            V result = action.call();
            acceptTime(measuredProcesses, clock.millis() - start);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    public void measureTime(MeasuredProcesses measuredProcesses, Runnable action) {
        long start = clock.millis();
        action.run();
        acceptTime(measuredProcesses, clock.millis() - start);
    }

    public void printStatistics() {
        System.out.printf("%1$s Time Statistics %1$s%n", "=".repeat(40));
        timeStatistics.forEach((process, statistics) -> System.out.printf("%s - %s%n", process, statistics));
    }

    public long currentMillis() {
        return clock.millis();
    }

    public enum MeasuredProcesses {
        SELECTION,
        MUTATION,
        EVALUATION,
        REPLACEMENT,
        CROSSOVER,
        ALGORITHM_DURATION
    }
}
