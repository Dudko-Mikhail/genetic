package by.dudko.genetic.util;

import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public final class RandomUtils {
    public static IntStream randomIndexes(RandomGenerator random, int startIndex, int endIndex) {
        return IntStream.range(startIndex, endIndex)
                .filter(index -> random.nextBoolean());
    }

    public static IntStream randomIndexes(RandomGenerator random, int startIndex, int endIndex, int size) {
        if (endIndex - startIndex < size) {
            throw new IllegalArgumentException(
                    String.format("Size must be less than endIndex - startIndex (%d). Actual size: %d",
                            endIndex - startIndex, size));
        }
        return IntStream.generate(() -> random.nextInt(startIndex, endIndex))
                .distinct()
                .limit(size);
    }

    public static IntStream randomIndexes(RandomGenerator random, int startIndex, int endIndex,
                                          double selectionProbability) {
        RequireUtils.probability(selectionProbability);
        return IntStream.range(startIndex, endIndex)
                .filter(index -> random.nextDouble() < selectionProbability);
    }

    public static boolean roll(RandomGenerator random, double probability) {
        return random.nextDouble() < probability;
    }

    private RandomUtils() {
    }
}
