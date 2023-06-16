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

    public abstract G fromNumberWithSameBounds(Number value);

    @Override
    public int compareTo(G other) {
        return comparator.compare(value, other.value);
    }

    public int intValue() {
        return value.intValue();
    }

    public long longValue() {
        return value.longValue();
    }

    public float floatValue() {
        return value.floatValue();
    }

    public double doubleValue() {
        return value.doubleValue();
    }

    public byte byteValue() {
        return value.byteValue();
    }

    public short shortValue() {
        return value.shortValue();
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
