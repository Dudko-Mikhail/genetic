package by.dudko.genetic.model.gene;

import java.util.Objects;

public class BaseGene<T> implements Gene<T, BaseGene<T>> {
    private final T value;

    public BaseGene(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public BaseGene<T> newInstance(T value) {
        return new BaseGene<>(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseGene<?> gene = (BaseGene<?>) o;
        return Objects.equals(value, gene.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return Objects.toString(value);
    }
}
