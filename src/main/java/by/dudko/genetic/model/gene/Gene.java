package by.dudko.genetic.model.gene;

import java.util.Objects;

public class Gene<T> {
    private final T value;

    public Gene(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gene<?> gene = (Gene<?>) o;
        return Objects.equals(value, gene.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
