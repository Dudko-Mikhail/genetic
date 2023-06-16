package by.dudko.genetic.util;

import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public final class RandomUtils {
    public static IntStream uniqueRandomNumbers(RandomGenerator random, int start, int endExclusive, int size) {
        if (endExclusive - start < size) {
            throw new IllegalArgumentException(
                    String.format("Size must be less than endExclusive - start (%d). Actual size: %d",
                            endExclusive - start, size));
        }
        return IntStream.generate(() -> random.nextInt(start, endExclusive))
                .distinct()
                .limit(size);
    }

    public static IntStream randomNumbers(RandomGenerator random, int start, int endExclusive, int size) {
        return IntStream.generate(() -> random.nextInt(start, endExclusive))
                .limit(size);
    }

    public static IntStream uniqueRandomNumbers(RandomGenerator random, int start, int endExclusive,
                                                double selectionProbability) {
        RequireUtils.probability(selectionProbability);
        return IntStream.range(start, endExclusive)
                .filter(index -> spin(random, selectionProbability));
    }

    public static IntStream uniqueRandomNumbers(RandomGenerator random, int start, int endExclusive) {
        return IntStream.range(start, endExclusive)
                .filter(index -> random.nextBoolean());
    }

    public static boolean spin(RandomGenerator random, double probability) {
        return random.nextDouble() < probability;
    }

    private RandomUtils() {
    }
}
