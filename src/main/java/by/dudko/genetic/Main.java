package by.dudko.genetic;

import by.dudko.genetic.util.RandomUtils;

import java.util.*;
import java.util.function.Function;

public class Main { // todo add random holder; Добавить аннотацию функционального интерфейса
    // todo Избавиться от завязки на конкретную реализацию можно путём создания метода/конструктора в интерфейсе
    public static void main(String[] args) throws Exception {

    }


    // todo Мутация - унарный оператов; скрещивание - бинарный оператор
    // todo Можно дабавить валидатов как поле Инициализатора.
    // todo Удалить пакет stepProcess



    void testPerformance() {
        Random random = new Random();
        long start;
        long end;

        DoubleSummaryStatistics oldSolution = new DoubleSummaryStatistics();
        for (int i = 0; i < 5; i++) {
            start = System.currentTimeMillis();
            var arr = RandomUtils.randomIndexes(random, 0, 100000000, 20000000)
                    .toArray();
            end = System.currentTimeMillis();
            System.out.println(arr.length);
            oldSolution.accept(end - start);
        }

        System.out.println("Stream solution: " + oldSolution.getAverage());
    }
}


final class Probabilities {
    private Probabilities() {
    }

    private static final long RANGE =
            (long) Integer.MAX_VALUE - (long) Integer.MIN_VALUE;

    /**
     * Maps the probability, given in the range {@code [0, 1]}, to an
     * integer in the range {@code [Integer.MIN_VALUE, Integer.MAX_VALUE]}.
     *
     * @param probability the probability to widen.
     * @return the widened probability.
     * @see #toFloat(int)
     */
    public static int toInt(final double probability) {
        return (int) (RANGE * probability + Integer.MIN_VALUE);
    }

    /**
     * Maps the <i>integer</i> probability, within the range
     * {@code [Integer.MIN_VALUE, Integer.MAX_VALUE]} back to a float
     * probability within the range {@code [0, 1]}.
     *
     * @param probability the <i>integer</i> probability to map.
     * @return the mapped probability within the range {@code [0, 1]}.
     * @see #toInt(double)
     */
    public static float toFloat(final int probability) {
        final long value = (long) probability + Integer.MAX_VALUE + 1;
        return (float) (value / (double) RANGE);
    }
}

class ZebraContainer<T extends Comparable<? super T>> {
    public T value;

    ZebraContainer(Comparator<T> comparator) {

    }
}

// todo remove
class CHROMOSOME<G extends Gen<?>> { // todo Умная и обчычная хромосома должны имплементировать один интерфейс
    List<G> genes = new ArrayList<>();

    public void addGene(G gen) {
        genes.add(gen);
    }

    public List<G> getGenes() {
        return genes;
    }

    public G getGene(int index) {
        return genes.get(index);
    }
}

class CleverCHROMOSOME<V, G extends Gen<V>> {
    List<G> genes = new ArrayList<>();

    public void addGene(G gen) {
        genes.add(gen);
    }

    public List<G> getGenes() {
        return genes;
    }

    public G getGene(int index) {
        return genes.get(index);
    }

    public V getGeneValue(int index) { // нарушение закона Диметры/принципа наименьшей осведомлённости)
        return genes.get(index).value;
    }
}

class Gen<T> {
    T value;

    public Gen(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return  "value: " + value;
    }
}

class POPULATION<C extends CHROMOSOME<?>> {
    private final List<C> chromosomes = new ArrayList<>();

    public List<C> getChromosomes() {
        return chromosomes;
    }
}

class Zeta { // todo watch
    public static void main(String[] args) {
        Gen<Integer> integerGen = new Gen<>(9);
        CHROMOSOME<Gen<Integer>> chromosome = new CHROMOSOME<>();
        POPULATION<CHROMOSOME<Gen<Integer>>> population;
        chromosome.addGene(integerGen);
    }
}



// todo Важно!!! Подумать над выносом интерфейса indexSelector и использование его в мутации и скрещивании
// todo Вместо T написать свой интерфейс<T> с методами: T get(index), size(), getCollection(), replace(index, T object);
// todo введение интерфейса поменяет реализацию Mutation/Crossover builder: поле indexSelector вместо вероянтостей.
interface IndexSelector<T> {
    Set<Integer> select(T type);
}




























