package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.BinaryCrossover;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.random.RandomGenerator;

public class UniformCrossoverWithMask<G extends Gene<?, G>, F> extends BinaryCrossover<G, F> {
    private final boolean[] mask;

    public UniformCrossoverWithMask(RandomGenerator random, boolean[] mask) {
        super(random);
        this.mask = Arrays.copyOf(mask, mask.length);
    }

    @Override
    public List<Chromosome<G>> performCrossover(Chromosome<G> first, Chromosome<G> second) {
        int length = Math.min(mask.length, Math.min(first.length(), second.length()));
        RequireUtils.lessOrEqual(length, mask.length);
        List<G> firstGenes = new ArrayList<>(first.getGenes());
        List<G> secondGenes = new ArrayList<>(second.getGenes());
        for (int i = 0; i < length; i++) {
            if (mask[i]) {
                firstGenes.set(i, second.getGene(i));
                secondGenes.set(i, first.getGene(i));
            }
        }
        return List.of(first.newInstance(firstGenes), second.newInstance(secondGenes));
    }
}
