package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.IndexSelector;

import java.util.List;
import java.util.random.RandomGenerator;

public abstract class BinaryCrossover<G extends Gene<?, G>, F> extends ChromosomeBasedCrossover<G, F> {
    public BinaryCrossover(RandomGenerator random) {
        super(random, 2);
    }

    protected BinaryCrossover(IndexSelector<Individual<G, F>> chromosomeSelector) {
        super(chromosomeSelector);
    }

    @Override
    protected final List<Chromosome<G>> performCrossover(List<? extends Chromosome<G>> participants) {
        return performCrossover(participants.get(0), participants.get(1));
    }

    public abstract List<Chromosome<G>> performCrossover(Chromosome<G> first, Chromosome<G> second);
}
