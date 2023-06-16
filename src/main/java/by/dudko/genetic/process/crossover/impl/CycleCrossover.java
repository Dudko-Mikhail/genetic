package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.BaseChromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.model.gene.IntegerGene;
import by.dudko.genetic.process.crossover.BinaryCrossover;

import java.util.*;
import java.util.random.RandomGenerator;

public class CycleCrossover<G extends Gene<?, G>, F> extends BinaryCrossover<G, F> {
    private final RandomGenerator random;

    public CycleCrossover(RandomGenerator random) {
        super(random);
        this.random = random;
    }

    @Override
    public List<Chromosome<G>> performCrossover(Chromosome<G> first, Chromosome<G> second) {
        if (first.length() != second.length()) {
            throw new IllegalArgumentException(
                    "Chromosomes with same length are required: %s != %s".formatted(first.length(), second.length()));
        }

        List<G> secondParentGenes = second.getGenes();

        List<G> firstGenes = new ArrayList<>(secondParentGenes);
        List<G> secondGenes = new ArrayList<>(first.getGenes());

        int startIndex = random.nextInt(first.length());
        var firstCycyle = defineCycle(firstGenes, secondGenes, startIndex);
        var secondCycle = defineCycle(secondGenes, firstGenes, startIndex);

        for (Integer index : firstCycyle) {
            firstGenes.set(index, secondGenes.get(index));
        }

        for (Integer index : secondCycle) {
            secondGenes.set(index, secondParentGenes.get(index));
        }
        return List.of(first.newInstance(firstGenes), second.newInstance(secondGenes));
    }

    private List<Integer> defineCycle(List<G> first, List<G> second, int startIndex) {
        int currentIndex = startIndex;
        List<Integer> cycle = new ArrayList<>();
        Set<Integer> usedIndexes = new HashSet<>();
        int previousSize = 0;

        while (true) {
            usedIndexes.add(currentIndex);
            if (previousSize == usedIndexes.size()) {
                break;
            }
            previousSize++;
            cycle.add(currentIndex);
            G currentGene = first.get(currentIndex);
            currentIndex = second.indexOf(currentGene);
        }
        return cycle;
    }

    public static void main(String[] args) {
        CycleCrossover<IntegerGene, Object> crossover = new CycleCrossover<>(new Random());
        Chromosome<IntegerGene> first = new BaseChromosome<>(
                List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), IntegerGene::new
        );
        Chromosome<IntegerGene> second = new BaseChromosome<>(
                List.of(4, 1, 2, 8, 7, 6, 9, 3, 5), IntegerGene::new
        );
        crossover.performCrossover(first, second).forEach(c -> System.out.println(c.getValues(Integer.class)));
    }
}
