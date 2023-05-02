package by.dudko.genetic.statistics;

import java.util.HashMap;
import java.util.Map;

public class Statistics { // todo добавить параметризацию
    private Map<String, Metric> metrics = new HashMap<>();

    public void addMetric(Metric metric) {
        metrics.put(metric.name(), metric);
    }

    public Metric getMetric(String name) {
        return metrics.get(name);
    }
}
