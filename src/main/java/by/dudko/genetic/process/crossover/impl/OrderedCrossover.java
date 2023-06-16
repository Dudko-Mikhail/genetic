package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.BaseChromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.model.gene.IntegerGene;
import by.dudko.genetic.process.crossover.BinaryCrossover;
import by.dudko.genetic.util.RandomUtils;

import java.util.*;
import java.util.random.RandomGenerator;

public class OrderedCrossover<G extends Gene<?, G>, F> extends BinaryCrossover<G, F> {
    private final RandomGenerator random;

    public OrderedCrossover(RandomGenerator random) {
        super(random);
        this.random = random;
    }

    @Override
    public List<Chromosome<G>> performCrossover(Chromosome<G> first, Chromosome<G> second) {
        int length = first.length();
        if (length != second.length()) {
            throw new IllegalArgumentException(
                    "Chromosomes with same length are required: %s != %s".formatted(first.length(), second.length()));
        }

        List<G> firstParentGenes = first.getGenes();
        List<G> secondParentGenes = second.getGenes();

        int[] positions = RandomUtils.uniqueRandomNumbers(random, 1, length - 1, 2)
                .sorted()
                .toArray();

        Set<G> firstGenesSequence = new LinkedHashSet<>(firstParentGenes.subList(positions[0], positions[1]));
        Set<G> secondGenesSequence = new LinkedHashSet<>(secondParentGenes.subList(positions[0], positions[1]));

        List<G> firstGenes = new ArrayList<>(firstParentGenes);
        List<G> secondGenes = new ArrayList<>(secondParentGenes);

        for (int i = 0, j = positions[1]; i < length; i++, j++) {
            int index = j % length;
            firstGenesSequence.add(secondParentGenes.get(index));
            secondGenesSequence.add(firstParentGenes.get(index));
        }

        var arr1 = firstGenesSequence.toArray();
        var arr2 = secondGenesSequence.toArray();
        for (int i = positions[1], j = positions[1] - positions[0]; i % length != positions[0]; i++, j++) {
            int index = i % length;
            firstGenes.set(index, (G) arr1[j]);
            secondGenes.set(index, (G) arr2[j]);
        }
        return List.of(first.newInstance(firstGenes), second.newInstance(secondGenes));
    }

    public static void main(String[] args) {
        OrderedCrossover<IntegerGene, Object> crossover = new OrderedCrossover<>(new Random());
        Chromosome<IntegerGene> first = new BaseChromosome<>(
                List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), IntegerGene::new
        );
        Chromosome<IntegerGene> second = new BaseChromosome<>(
                List.of(4, 5, 2, 1, 8, 7, 6, 9, 3), IntegerGene::new
        );
        crossover.performCrossover(first, second).forEach(c -> System.out.println(c.getValues(Integer.class)));
    }
}
