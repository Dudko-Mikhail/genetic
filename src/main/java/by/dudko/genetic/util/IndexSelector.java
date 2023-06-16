package by.dudko.genetic.util;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface IndexSelector<T> {
    IntStream selectIndexes(List<? extends T> list);

    default Stream<T> select(List<? extends T> list) {
        return selectIndexes(list)
                .mapToObj(list::get);
    }
}
