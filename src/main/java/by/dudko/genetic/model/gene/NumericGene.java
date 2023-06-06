package by.dudko.genetic.model.gene;

import java.util.Comparator;
import java.util.Objects;

public abstract class NumericGene<T extends Number, G extends NumericGene<T, G>> implements LimitedGene<T, G>,
        Comparable<G> {
    protected final T min;
    protected final T max;
    protected final T value;
    protected final Comparator<T> comparator;

    protected NumericGene(T value, T min, T max, Comparator<T> comparator) {
        this.comparator = Objects.requireNonNull(comparator);
        this.max = max;
        this.min = min;
        if (!isInBounds(value)) {
            throw new IllegalArgumentException("Value must be in segment [min, max]");
        }
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public T min() {
        return min;
    }

    @Override
    public T max() {
        return max;
    }

    @Override
    public boolean isInBounds(T value, T min, T max) {
        return LimitedGene.isInBounds(value, min, max, comparator);
    }

    @Override
    public int compareTo(G other) {
        return comparator.compare(value, other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericGene<?, ?> that = (NumericGene<?, ?>) o;
        return Objects.equals(min, that.min) && Objects.equals(max, that.max)
                && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max, getValue());
    }

    @Override
    public String toString() {
        return "{min: [%s], max: [%s], value: [%s]}".formatted(min, max, value);
    }
}
