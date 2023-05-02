package by.dudko.genetic.model;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.process.evaluation.FitnessFunction;

import java.util.*;

public class Population<T, F> { // todo add fitness storage to chromosome?
    private final List<Chromosome<T>> chromosomes;
    private final List<F> populationFitness;
    private final FitnessFunction<T, ? extends F> fitnessFunction;

    public Population(List<Chromosome<T>> chromosomes, FitnessFunction<T, ? extends F> fitnessFunction) {
        this.chromosomes = new ArrayList<>(chromosomes);
        this.fitnessFunction = Objects.requireNonNull(fitnessFunction);
        populationFitness = new ArrayList<>();
    }

    public void evaluatePopulation() {
        if (nonEvaluated()) {
            chromosomes.forEach(chromosome -> populationFitness.add(fitnessFunction.apply(chromosome)));
        }
    }

    public Optional<Chromosome<T>> getFittest(Comparator<? super F> comparator) { // todo можно вычислять одновременно в методе evaluate
        if (nonEvaluated()) { // todo maybe exception
            return Optional.empty();
        }
        return chromosomes.stream()
                .max(toChromosomeComparator(Objects.requireNonNull(comparator)));
    }

    private Comparator<Chromosome<T>> toChromosomeComparator(Comparator<? super  F> comparator) {
        return (a, b) -> comparator.compare(fitnessFunction.apply(a), fitnessFunction.apply(b));
    }

    public boolean isEvaluated() {
        return !populationFitness.isEmpty();
    }

    public boolean nonEvaluated() {
        return populationFitness.isEmpty();
    }

    public List<Chromosome<T>> getChromosomes() {
        return Collections.unmodifiableList(chromosomes);
    }

    public void addAll(Collection<? extends Chromosome<T>> collection) {
        chromosomes.addAll(collection);
    }

    public Chromosome<T> getChromosome(int index) {
        return chromosomes.get(index);
    }

    public F getFitness(int index) {
        return populationFitness.get(index);
    }

    public int getSize() {
        return chromosomes.size();
    }

    public FitnessFunction<T, ? extends F> getFitnessFunction() {
        return fitnessFunction;
    }
}
