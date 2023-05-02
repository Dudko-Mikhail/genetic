package by.dudko.genetic.model;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.process.evaluation.FitnessFunction;

public class Genotype<T, F> { // todo equals, hashcode
    private final Chromosome<T> chromosome;
    private final long birthGeneration;
    private F fitness;

    public Genotype(Chromosome<T> chromosome, long birthGeneration) {
        this.chromosome = chromosome;
        this.birthGeneration = birthGeneration;
    }

    public Genotype(Chromosome<T> chromosome, long birthGeneration, F fitness) {
        this(chromosome, birthGeneration);
        this.fitness = fitness;
    }

    long getAge(long currentGeneration) {
        return currentGeneration - birthGeneration;
    }

    public F getFitness() {
        return fitness;
    }

    public void setFitness(F fitness) {
        this.fitness = fitness;
    }

    public F evaluateAndSetFitness(FitnessFunction<T, F> fitnessFunction) {
        fitness = fitnessFunction.apply(chromosome);
        return fitness;
    }

    public boolean isEvaluated() {
        return fitness != null;
    }

    public boolean nonEvaluated() {
        return fitness == null;
    }
}
