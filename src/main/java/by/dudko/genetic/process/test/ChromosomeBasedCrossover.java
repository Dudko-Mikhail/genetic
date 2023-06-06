package by.dudko.genetic.process.test;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.PopulationCrossover;
import by.dudko.genetic.util.IndexSelector;
import by.dudko.genetic.util.SelectorsFactory;

import java.util.Objects;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public abstract class ChromosomeBasedCrossover<G extends Gene<?, G>> implements PopulationCrossover<G> {
    private final Selector<G> selector;
    private final int selectionSize;

    protected ChromosomeBasedCrossover(RandomGenerator random, int selectionSize) {
        this(Selectors.randomSelector(random), selectionSize);
    }

    public ChromosomeBasedCrossover(Selector<G> selector, int selectionSize) {
        this.selector = Objects.requireNonNull(selector);
        this.selectionSize = selectionSize;
    }

    @Override
    public final Population<G, ?> performCrossover(Population<G, ?> population, int offspringSize) {
        var offspring = Stream.generate(() -> {
                    Chromosome<G>[] chromosomes = selector.select(population, selectionSize);
                    return performCrossover(chromosomes);
                })
                .limit(offspringSize)
                .toList();
        return new Population<>(offspring, population.getFitnessFunction());
    }

    protected abstract Chromosome<G> performCrossover(Chromosome<G>[] participants);
}

abstract class ChromosomeBasedCrossoverWithIndexSelector<G extends Gene<?, G>> implements PopulationCrossover<G> {
    private final IndexSelector indexSelector;

    public ChromosomeBasedCrossoverWithIndexSelector(IndexSelector indexSelector) {
        this.indexSelector = indexSelector;
    }

    @Override
    public Population<G, ?> performCrossover(Population<G, ?> population, int offspringSize) {

        var offspring = Stream.generate(() -> {
                    Chromosome<G>[] individuals = indexSelector.selectIndexes(population.getSize())
                            .mapToObj(population::getIndividual)
                            .toArray(Chromosome[]::new);
                    return performCrossover(individuals);
                })
                .limit(offspringSize)
                .toList();
        return new Population<>(offspring, population.getFitnessFunction());
    }

    protected abstract Chromosome<G> performCrossover(Chromosome<G>[] participants);
}


interface Selector<G extends Gene<?, G>> {
    Chromosome<G>[] select(Population<G, ?> population, int count);
}

class Selectors {
    public static <G extends Gene<?, G>> Selector<G> randomSelector(RandomGenerator random) {
        return (population, count) -> {
            int size = population.getSize();
            Chromosome<G>[] chromosomes = new Chromosome[count];
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


abstract class BinaryCrossover<G extends Gene<?, G>> extends ChromosomeBasedCrossover<G> implements BinaryOperator<Chromosome<G>> {
    protected BinaryCrossover(RandomGenerator random) {
        super(random, 2);
    }

    protected BinaryCrossover(Selector<G> selector) {
        super(selector, 2);
    }

    @Override
    protected final Chromosome<G> performCrossover(Chromosome<G>[] participants) {
        return apply(participants[0], participants[1]);
    }
}