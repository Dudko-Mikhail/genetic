package by.dudko.genetic.model.gene;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class EnumGene<T> implements Gene<T, EnumGene<T>> { // todo equals, hashcode
    private final T value;
    private final Set<T> validValues;

    public EnumGene(T value, Set<T> validValues) {
        this.value = value;
        this.validValues = Objects.requireNonNull(validValues);
        if (!validValues.contains(value)) {
            throw new IllegalArgumentException("Given value is invalid. %s".formatted(value));
        }
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public EnumGene<T> newInstance(T value) {
        return new EnumGene<>(value, validValues);
    }

    public Map<T, Integer> getIndexMap() {
        int i = 0;
        Map<T, Integer> indexMap = new HashMap<>(validValues.size());
        for (T value : validValues) {
            indexMap.put(value, i++);
        }
        return indexMap;
    }
}
