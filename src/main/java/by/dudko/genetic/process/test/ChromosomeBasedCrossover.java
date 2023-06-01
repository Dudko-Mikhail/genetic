package by.dudko.genetic.process.test;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BaseGene;
import by.dudko.genetic.process.crossover.PopulationCrossover;
import by.dudko.genetic.process.evaluation.FitnessFunction;
import by.dudko.genetic.process.initialization.GeneBasedInitializer;
import by.dudko.genetic.util.IndexSelector;
import by.dudko.genetic.util.SelectorsFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public abstract class ChromosomeBasedCrossover<T> implements PopulationCrossover<T> {
    private final Selector<T> selector;
    private final int selectionSize;

    protected ChromosomeBasedCrossover(RandomGenerator random, int selectionSize) {
        this(Selectors.randomSelector(random), selectionSize);
    }

    public ChromosomeBasedCrossover(Selector<T> selector, int selectionSize) {
        this.selector = Objects.requireNonNull(selector);
        this.selectionSize = selectionSize;
    }

    @Override
    public final Population<T, ?> performCrossover(Population<T, ?> population, int offspringSize) {
        var offspring = Stream.generate(() -> {
                    Chromosome<T>[] chromosomes = selector.select(population, selectionSize);
                    return performCrossover(chromosomes);
                })
                .limit(offspringSize)
                .toList();
        return new Population<>(offspring, population.getFitnessFunction());
    }

    protected abstract Chromosome<T> performCrossover(Chromosome<T>[] participants);
}

abstract class ChromosomeBasedCrossoverWithIndexSelector<T> implements PopulationCrossover<T> {
    private final IndexSelector indexSelector;

    public ChromosomeBasedCrossoverWithIndexSelector(IndexSelector indexSelector) {
        this.indexSelector = indexSelector;
    }

    @Override
    public Population<T, ?> performCrossover(Population<T, ?> population, int offspringSize) {

        var offspring = Stream.generate(() -> {
                    Chromosome<T>[] individuals = indexSelector.selectIndexes(population.getSize())
                            .mapToObj(population::getIndividual)
                            .toArray(Chromosome[]::new);
                    return performCrossover(individuals);
                })
                .limit(offspringSize)
                .toList();
        return new Population<>(offspring, population.getFitnessFunction());
    }

    protected abstract Chromosome<T> performCrossover(Chromosome<T>[] participants);
}


interface Selector<T> {
    Chromosome<T>[] select(Population<T, ?> population, int count);
}

class Selectors {
    public static <T> Selector<T> randomSelector(RandomGenerator random) {
        return (population, count) -> {
            int size = population.getSize();
            Chromosome<T>[] chromosomes = new Chromosome[count];
            for (int i = 0; i < count; i++) {
                chromosomes[i] = population.getIndividual(random.nextInt(size));
            }
            return chromosomes;
        };
    }

    public static void main(String[] args) {
//        Random random = new Random();
//        FitnessFunction<Integer, Double> ff = (chromosome) -> chromosome.getGene(0).getValue().doubleValue();
//
//        GeneBasedInitializer<Integer> initializer = new GeneBasedInitializer<>(() -> new BaseGene<>(random.nextInt(100)), () -> 5);
//        initializer.produceChromosomes(10);
//
//        Selector<Integer> selector = Selectors.randomSelector(new Random());
//        var chromosomes = selector.select(new Population<>(initializer.produceChromosomes(10).toList()), 4);
//        Arrays.stream(chromosomes)
//                .forEach(c -> System.out.println(ff.apply(c)));
        var selector = SelectorsFactory.fixedSizeWithUniqueIndexes(new Random(), 5);
        for (int i = 0; i < 3; i++) {
            System.out.println(selector.selectIndexes(7).boxed().toList());
        }
    }
}


abstract class BinaryCrossover<T> extends ChromosomeBasedCrossover<T> implements BinaryOperator<Chromosome<T>> {
    protected BinaryCrossover(RandomGenerator random) {
        super(random, 2);
    }

    protected BinaryCrossover(Selector<T> selector) {
        super(selector, 2);
    }

    @Override
    protected final Chromosome<T> performCrossover(Chromosome<T>[] participants) {
        return apply(participants[0], participants[1]);
    }
}