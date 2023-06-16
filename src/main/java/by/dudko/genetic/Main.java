package by.dudko.genetic;

import java.time.Clock;
import java.util.LongSummaryStatistics;
import java.util.Random;

public class Main {
    private static final Clock clock = Clock.systemUTC();
    private static final Random random = new Random();

    public static void main(String[] args) throws Exception {


//        var selector = SelectorsFactory.selectorWithVaryingSelectionLength(new Random(), 1, 10);
//        var binary = SelectorsFactory.toBinarySelector(selector);
//        System.out.println(selector.selectIndexes(20).boxed().toList());
//        System.out.println(selector.selectIndexes(9).boxed().toList());
//        System.out.println(selector.selectIndexes(9).boxed().toList());

    }

    public static LongSummaryStatistics measurePerformance(int repeatCount, Command command) {
        LongSummaryStatistics statistics = new LongSummaryStatistics();
        for (int i = 0; i < repeatCount; i++) {
            long start = clock.millis();
            command.execute();
            long end = clock.millis();
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