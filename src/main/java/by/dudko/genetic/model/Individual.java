package by.dudko.genetic.model;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BaseGene;
import by.dudko.genetic.process.evaluation.FitnessFunction;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Individual<T, F> implements Chromosome<T> { // todo equals, hashcode, boolean isEvaluated?
    private final Chromosome<T> chromosome;
    private long birthGeneration;
    private F fitness;

    public Individual(Chromosome<T> chromosome, long birthGeneration, F fitness) { // todo Разобраться с конструкторами конструкторы
        this(chromosome, birthGeneration);
        this.fitness = fitness;
    }

    public Individual(Chromosome<T> chromosome, long birthGeneration) {
        this(chromosome);
        this.birthGeneration = birthGeneration;
    }

    public Individual(Chromosome<T> chromosome, F fitness) {
        this(chromosome);
        this.fitness = fitness;
    }

    public Individual(Chromosome<T> chromosome) {
        this.chromosome = chromosome;
    }

    @Override
    public int length() {
        return chromosome.length();
    }

    @Override
    public BaseGene<T> getGene(int index) {
        return chromosome.getGene(index);
    }

    @Override
    public List<BaseGene<T>> getGenes() {
        return chromosome.getGenes();
    }

    @Override
    public BaseGene<T> replaceGene(int index, BaseGene<T> newGene) {
        fitness = null;
        return chromosome.replaceGene(index, newGene);
    }

    @Override
    public Chromosome<T> newInstance(Collection<? extends BaseGene<T>> genes) {
        return new Individual<>(chromosome.newInstance(genes), fitness);
    }

    public Chromosome<T> box() {
        return this;
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

    public F evaluateAndSetFitness(FitnessFunction<T, ? extends F> fitnessFunction) {
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
