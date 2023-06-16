package by.dudko.genetic.util;

import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public final class IndexSelectors { // todo реализовать через RandomUtils
    private IndexSelectors() {
    }

    public static <T> IndexSelector<T> probabilitySelector(RandomGenerator random, double probability) {
        Objects.requireNonNull(random);
        RequireUtils.probability(probability);
        return list -> IntStream.range(0, list.size())
                .filter(i -> random.nextDouble() < probability);
    }

    public static <T> IndexSelector<T> allSelector(int startIndex) {
        RequireUtils.nonNegative(startIndex);
        return list -> IntStream.range(startIndex, list.size());
    }

    public static <T> IndexSelector<T> selectorWithVaryingSelectionLength(RandomGenerator random, int minSizeInclusive,
                                                                          int maxSizeExclusive) {
        RequireUtils.positive(minSizeInclusive);
        RequireUtils.less(minSizeInclusive, maxSizeExclusive);
        IntSupplier supplier = () -> random.nextInt(minSizeInclusive, maxSizeExclusive);
        return list -> IntStream.generate(() -> random.nextInt(list.size()))
                .limit(supplier.getAsInt());
    }

    public static <T> IndexSelector<T> fixedSizeSelector(RandomGenerator random, int size) {
        Objects.requireNonNull(random);
        RequireUtils.nonNegative(size);
        return list -> IntStream.generate(() -> random.nextInt(list.size()))
                .limit(size);
    }

    public static <T> IndexSelector<T> fixedSizeWithUniqueIndexes(RandomGenerator random, int size) {
        Objects.requireNonNull(random);
        RequireUtils.nonNegative(size);
        return list -> {
            int maxValue = list.size();
            RequireUtils.lessOrEqual(size, maxValue);
            return IntStream.generate(() -> random.nextInt(maxValue))
                    .distinct()
                    .limit(size);
        };
    }
}
