package by.dudko.genetic.util;

import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public final class SelectorsFactory { // todo реализовать через RandomUtils
    private SelectorsFactory() {
    }

    public static IndexSelector probabilitySelector(RandomGenerator random, double probability) {
        Objects.requireNonNull(random);
        RequireUtils.probability(probability);
        return (maxValue) -> IntStream.range(0, maxValue)
                .filter(i -> random.nextDouble() < probability);
    }

    public static IndexSelector selectorWithVaryingSelectionLength(RandomGenerator random, int minSizeInclusive,
                                                                   int maxSizeExclusive) {
        RequireUtils.positive(minSizeInclusive);
        RequireUtils.less(minSizeInclusive, maxSizeExclusive);
        IntSupplier supplier = () -> random.nextInt(minSizeInclusive, maxSizeExclusive);
        return (maxValue) -> IntStream.generate(() -> random.nextInt(maxValue))
                .limit(supplier.getAsInt());
    }

    public static IndexSelector fixedSizeSelector(RandomGenerator random, int size) {
        Objects.requireNonNull(random);
        RequireUtils.nonNegative(size);
        return (maxValue) -> IntStream.generate(() -> random.nextInt(maxValue))
                .limit(size);
    }

    public static IndexSelector fixedSizeWithUniqueIndexes(RandomGenerator random, int size) {
        Objects.requireNonNull(random);
        RequireUtils.nonNegative(size);
        return (maxValue) -> {
            RequireUtils.lessOrEqual(size, maxValue);
            return IntStream.generate(() -> random.nextInt(maxValue))
                    .distinct()
                    .limit(size);
        };
    }

    public static IndexSelector toBinarySelector(IndexSelector indexSelector) {
        return (maxValueExclusive -> indexSelector.selectIndexes(maxValueExclusive)
                .limit(2));
    }
}
