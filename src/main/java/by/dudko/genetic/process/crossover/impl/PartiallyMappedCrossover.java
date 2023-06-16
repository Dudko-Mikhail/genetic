package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.BaseChromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.model.gene.IntegerGene;
import by.dudko.genetic.process.crossover.BinaryCrossover;
import by.dudko.genetic.util.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class PartiallyMappedCrossover<G extends Gene<?, G>, F> extends BinaryCrossover<G, F> { // todo EnumGene
    private final RandomGenerator random;

    public PartiallyMappedCrossover(RandomGenerator random) {
        super(random);
        this.random = random;
    }

    @Override
    public List<Chromosome<G>> performCrossover(Chromosome<G> first, Chromosome<G> second) {
        if (first.length() != second.length()) {
            throw new IllegalArgumentException(
                    "Chromosomes with same length are required: %s != %s".formatted(first.length(), second.length()));
        }
        int length = first.length();
        int[] positions = RandomUtils.uniqueRandomNumbers(random, 1, length - 1, 2)
                .sorted()
                .toArray();

        List<G> firstGenes = new ArrayList<>(first.getGenes());
        List<G> secondGenes = new ArrayList<>(second.getGenes());

        List<G> firstPart = firstGenes.subList(positions[0], positions[1]);
        List<G> secondPart = secondGenes.subList(positions[0], positions[1]);


        for (int i = positions[0]; i < positions[1]; i++) {
            G oldValue = firstGenes.set(i, secondGenes.get(i));
            secondGenes.set(i, oldValue);
        }

        for (int i = 0; i < positions[0]; i++) {
            firstGenes.set(i, defineGene(firstPart, secondPart, firstGenes.get(i)));
            secondGenes.set(i, defineGene(secondPart, firstPart, secondGenes.get(i)));
        }


        for (int i = positions[1]; i < length; i++) {
            firstGenes.set(i, defineGene(firstPart, secondPart, firstGenes.get(i)));
            secondGenes.set(i, defineGene(secondPart, firstPart, secondGenes.get(i)));
        }

        return List.of(first.newInstance(firstGenes), second.newInstance(secondGenes));
    }

    private G defineGene(List<G> firstPart, List<G> secondPart, G startGene) {
        G currentGene = startGene;
        int current = firstPart.indexOf(currentGene);
        while (current != -1) {
            currentGene = secondPart.get(current);
            current = firstPart.indexOf(currentGene);
        }
        return currentGene;
    }

    public static void main(String[] args) {
        PartiallyMappedCrossover<IntegerGene, Object> crossover = new PartiallyMappedCrossover<>(new Random());
        Chromosome<IntegerGene> first = new BaseChromosome<>(
                List.of(1, 2, 3, 4, 5, 6, 7), IntegerGene::new
        );
        Chromosome<IntegerGene> second = new BaseChromosome<>(
                List.of(5, 4, 6, 7, 2, 1, 3), IntegerGene::new
        );
        crossover.performCrossover(first, second).forEach(c -> System.out.println(c.getValues(Integer.class)));
    }
}
