package by.dudko.genetic.util;

import java.util.stream.IntStream;

public interface IndexSelector {
    IntStream selectIndexes(int maxValueExclusive);
}
