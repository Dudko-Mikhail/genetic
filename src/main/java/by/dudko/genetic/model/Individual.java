package by.dudko.genetic.model;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.evaluation.FitnessFunction;

import java.util.Collection;
import java.util.List;

public class Individual<G extends Gene<?, G>, F> implements Chromosome<G> { // todo equals, hashcode, boolean isEvaluated?
    private final Chromosome<G> chromosome;
    private long birthGeneration;
    private F fitness;

    public Individual(Chromosome<G> chromosome, long birthGeneration, F fitness) { // todo Разобраться с конструкторами конструкторы
        this(chromosome, birthGeneration);
        this.fitness = fitness;
    }

    public Individual(Chromosome<G> chromosome, long birthGeneration) {
        this(chromosome);
        this.birthGeneration = birthGeneration;
    }

    public Individual(Chromosome<G> chromosome, F fitness) {
        this(chromosome);
        this.fitness = fitness;
    }

    public Individual(Chromosome<G> chromosome) {
        this.chromosome = chromosome;
    }

    @Override
    public int length() {
        return chromosome.length();
    }

    @Override
    public G getGene(int index) {
        return chromosome.getGene(index);
    }

    @Override
    public List<G> getGenes() {
        return chromosome.getGenes();
    }

    @Override
    public G replaceGene(int index, G newGene) {
        fitness = null;
        return chromosome.replaceGene(index, newGene);
    }

    @Override
    public Individual<G, F> newInstance(Collection<? extends G> genes) {
        return new Individual<>(chromosome.newInstance(genes), fitness);
    }

    public Chromosome<G> box() {
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

    public F evaluateAndSetFitness(FitnessFunction<G, ? extends F> fitnessFunction) {
        fitness = fitnessFunction.apply(chromosome);
        return fitness;
    }

    public boolean isEvaluated() {
        return fitness != null;
    }

    public boolean nonEvaluated() {
        return fitness == null;
    }

    @Override
    public String toString() { // todo update
        return "Individual{" +
                "chromosome=" + chromosome +
                ", fitness=" + fitness +
                '}';
    }
}
