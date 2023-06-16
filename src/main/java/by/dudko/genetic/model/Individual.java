package by.dudko.genetic.model;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.BaseChromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.evaluation.FitnessFunction;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Individual<G extends Gene<?, G>, F> extends BaseChromosome<G> { // todo equals, hashcode
    private long birthGeneration;
    private F fitness;

    public Individual(Chromosome<G> chromosome, long birthGeneration, F fitness) { // todo Разобраться с конструкторами
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
        super(chromosome.getGenes());
    }

    public Individual(Collection<? extends G> genes) {
        super(genes);
    }

    @Override
    public Individual<G, F> newInstance(Collection<? extends G> genes) {
        return new Individual<>(genes);
    }

    @Override
    public Individual<G, F> newInstance(G[] genes) {
        return new Individual<>(Arrays.stream(genes)
                .toList());
    }

    public Chromosome<G> box() {
        return this;
    }

    long getAge(long currentGeneration) {
        return currentGeneration - birthGeneration;
    }

    public void setBirthGeneration(long birthGeneration) {
        this.birthGeneration = birthGeneration;
    }

    public F getFitness() {
        return fitness;
    }

    public void setFitness(F fitness) {
        this.fitness = fitness;
    }

    public F evaluateAndSetFitness(FitnessFunction<G, ? extends F> fitnessFunction) {
        fitness = fitnessFunction.apply(this);
        return fitness;
    }

    public boolean isEvaluated() {
        return fitness != null;
    }

    public boolean nonEvaluated() {
        return fitness == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Individual<?, ?> that)) return false;
        if (!super.equals(o)) return false;

        if (birthGeneration != that.birthGeneration) return false;
        return Objects.equals(fitness, that.fitness);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (birthGeneration ^ (birthGeneration >>> 32));
        result = 31 * result + (fitness != null ? fitness.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Individual{");
        sb.append("genes=").append(getGenes());
        sb.append(", birthGeneration=").append(birthGeneration);
        sb.append(", fitness=").append(fitness);
        sb.append('}');
        return sb.toString();
    }
}
