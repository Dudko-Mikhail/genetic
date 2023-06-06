package by.dudko.genetic.statistics;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.RequireUtils;

import java.util.*;
import java.util.function.Consumer;

public class Statistics<G extends Gene<?, G>, F> implements IStatistics<G, F> {
    private final TimeStatistics timeStatistics = new TimeStatistics();
    private final int logPeriod;
    private final Map<String, Metric<G, F, ?>> metrics = new HashMap<>();
    private final Map<String, List<Object>> metricLog = new HashMap<>();
    private final Map<Long, StatisticsLog<G, F>> generationLog = new HashMap<>();
    private final Consumer<StatisticsLog<G, F>> printAction;

    public Statistics(int logPeriod) { // todo вместо этого парочку статических методов
        this(logPeriod, (StatisticsLog::printStatistics));
    }

    public Statistics(int logPeriod, Consumer<StatisticsLog<G, F>> printAction) {
        this.logPeriod = RequireUtils.positive(logPeriod);
        this.printAction = Objects.requireNonNull(printAction);
    }

    public Statistics<G, F> registerMetric(String name, Metric<G, F, ?> metric) {
        metrics.put(name, metric);
        return this;
    }

    public Metric<G, F, ?> getMetric(String name) {
        return metrics.get(name);
    }

    public void calculateStatistics(Population<G, F> population, long generationNumber) {
        if (generationNumber % logPeriod == 0) {
            metrics.forEach((name, metric) -> {
                List<Object> metricLog = this.metricLog.getOrDefault(name, new ArrayList<>());
                metricLog.add(metric.apply(population));
            });
            StatisticsLog<G, F> log = new StatisticsLog<>(generationNumber, population, metrics);
            generationLog.put(generationNumber, log);
            printAction.accept(log);
        }
    }

    public <T> Optional<T> select(long generationNumber, String metric, Class<T> loggedType) { // todo refactor
        if (generationNumber % logPeriod != 0) {
            return Optional.empty();
        }
        int index = (int) (generationNumber / logPeriod);
        List<Object> objects = metricLog.get(metric);
        return Optional.of(objects.get(index))
                .map(loggedType::cast);
    }

    public <T> List<T> select(String metric, Class<T> loggedType) {
        List<?> results = metricLog.get(metric);
        if (results == null) {
            return new ArrayList<>();
        }
        return results.stream()
                .map(loggedType::cast)
                .toList();
    }

    public Map<Long, StatisticsLog<G, F>> getGenerationLog() {
        return generationLog;
    }

    public TimeStatistics getTimeStatistics() {
        return timeStatistics;
    }




    @Override
    public <T> List<T> fullMetricLog(String metric, Class<T> loggedType) {
        return null;
    }

    @Override
    public <T> Optional<T> getLog(String metric, long generationNumber, Class<T> loggedType) {
        return Optional.empty();
    }

    @Override
    public <T> List<T> generationLog(long generationNumber, Class<T> loggedType) {
        return null;
    }

    @Override
    public void registerMetric(Metric<G, F, ?> metric) {

    }

    @Override
    public void printMetricLog(String metric) {

    }

    @Override
    public void printMetricLogByGeneration(String metric, long generationNumber) {
        var formatter = metrics.get(metric).formatter();
    }

    @Override
    public void printGenerationLog(long generationNumber) {
        generationLog.get(generationNumber).printStatistics();
    }

    @Override
    public void printFullStatistics() {

    }
}
