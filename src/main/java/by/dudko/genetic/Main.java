package by.dudko.genetic;

import by.dudko.genetic.util.RandomUtils;
import by.dudko.genetic.util.SelectorsFactory;

import java.util.DoubleSummaryStatistics;
import java.util.LongSummaryStatistics;
import java.util.Random;
import java.util.Set;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) throws Exception {
        var selector = SelectorsFactory.selectorWithVaryingSelectionLength(new Random(), 1, 10);
        var binary = SelectorsFactory.toBinarySelector(selector);
        System.out.println(selector.selectIndexes(20).boxed().toList());
        System.out.println(selector.selectIndexes(9).boxed().toList());
        System.out.println(selector.selectIndexes(9).boxed().toList());

    }

    static void testPerformance() {
        Random random = new Random();
        long start;
        long end;

        DoubleSummaryStatistics oldSolution = new DoubleSummaryStatistics();
        for (int i = 0; i < 5; i++) {
            start = System.currentTimeMillis();
            var arr = RandomUtils.uniqueRandomIndexes(random, 0, 100000000, 20000000)
                    .toArray();
            end = System.currentTimeMillis();
            System.out.println(arr.length);
            oldSolution.accept(end - start);
        }

        System.out.println("Stream solution: " + oldSolution.getAverage());
    }

    public static LongSummaryStatistics measurePerformance(int repeatCount, Command command) {
        LongSummaryStatistics statistics = new LongSummaryStatistics();
        for (int i = 0; i < repeatCount; i++) {
            long start = System.currentTimeMillis();
            command.execute();
            long end = System.currentTimeMillis();
            statistics.accept(end - start);
        }
        return statistics;
    }
}

@FunctionalInterface
interface Command {
    void execute();
}

class Problems {
    void toDo() {
        // todo Добавить сообщения к Require проверкам
        // todo add random holder; Добавить аннотацию функционального интерфейса
        // todo Избавиться от завязки на конкретную реализацию можно путём создания метода/конструктора в интерфейсе!

        // todo Нужен интерфейс селектор для выбора генов для мутации (сейчас вероятностный - так себе)
        // todo  Избавиться от интерфейса Gene или внедрить его повсеместно
        // todo стоп функция по времени
    }
}


// todo Важно!!! Подумать над выносом интерфейса indexSelector и использование его в мутации и скрещивании
// todo Вместо T написать свой интерфейс<T> с методами: T get(index), size(), getCollection(), replace(index, T object);
// todo введение интерфейса поменяет реализацию Mutation/Crossover builder: поле indexSelector вместо вероянтостей.
interface IndexSelector<T> {
    Set<Integer> select(T type);
}




























