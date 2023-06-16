package by.dudko.genetic.model;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.evaluation.FitnessFunction;

import java.util.*;

public class Population<G extends Gene<?, G>, F> { // todo для вычисления самого приспособленного компаратор можно получать через конструктор
    private final List<Individual<G, F>> individuals;
    private FitnessFunction<G, ? extends F> fitnessFunction;
    private boolean isEvaluated;

    public static <G extends Gene<?, G>, F> Comparator<Individual<G, F>> toIndividualsComparator(Comparator<? super F> fitnessComparator) {
        return (a, b) -> fitnessComparator.compare(a.getFitness(), b.getFitness());
    }

    public Population(Collection<? extends Chromosome<G>> chromosomes, FitnessFunction<G, ? extends F> fitnessFunction) {
        this(chromosomes);
        this.fitnessFunction = Objects.requireNonNull(fitnessFunction);
    }

    public Population(Collection<? extends Chromosome<G>> chromosomes) {
        this.individuals = new ArrayList<>(
                chromosomes.stream()
                        .map(this::wrap)
                        .toList()
        );
    }

    public Population(Chromosome<G>... chromosomes) {
        this(Arrays.stream(chromosomes)
                .toList());
    }


    public Population(FitnessFunction<G, F> fitnessFunction, Chromosome<G>... chromosomes) {
        this(Arrays.stream(chromosomes)
                .toList(), fitnessFunction);
    }

    public void evaluatePopulation() {
        if (isEvaluated) {
            return;
        }
        if (fitnessFunction == null) {
            throw new IllegalStateException("Cannot evaluate population without fitness function");
        }
        individuals.stream()
                .filter(Individual::nonEvaluated)
                .forEach(individual -> individual.evaluateAndSetFitness(fitnessFunction));
        isEvaluated = true;
    }

    public void evaluatePopulation(FitnessFunction<G, F> fitnessFunction) {
        this.fitnessFunction = Objects.requireNonNull(fitnessFunction);
        individuals.forEach(individual -> individual.evaluateAndSetFitness(fitnessFunction));
        isEvaluated = true;
    }

    public Optional<Individual<G, F>> getFittest(Comparator<? super F> comparator) {
        Objects.requireNonNull(comparator);
        if (nonEvaluated()) {
            evaluatePopulation();
        }
        return individuals.stream()
                .max(toIndividualsComparator(comparator));
    }

    public void sort(Comparator<? super F> fitnessComparator) {
        individuals.sort(toIndividualsComparator(fitnessComparator));
    }

    public boolean isEvaluated() {
        return isEvaluated;
    }

    public boolean nonEvaluated() {
        return !isEvaluated;
    }

    public List<Individual<G, F>> getIndividuals() {
        return Collections.unmodifiableList(individuals);
    }

    public void addAllWithoutEvaluation(Collection<? extends Chromosome<G>> chromosomes) {
        individuals.addAll(
                chromosomes.stream()
                        .map(this::wrap)
                        .toList()
        );
        // todo implement. Возраст нужно вынести в хромосому или хранить номер текущего поколения в популяции
    }

    public void addAllWithEvaluation(Collection<? extends Chromosome<G>> chromosomes) {
        individuals.addAll(
                chromosomes.stream()
                        .map(this::wrapAndEvaluate)
                        .toList()
        );
        // todo implement. Возраст нужно вынести в хромосому или хранить номер текущего поколения в популяции
    }

    public Individual<G, F> getIndividual(int index) {
        return individuals.get(index);
    }

    public Individual<G, F> replaceIndividual(int index, Chromosome<G> newChromosome) {
        return individuals.set(index, wrapAndEvaluate(newChromosome));
    }

    private Individual<G, F> wrap(Chromosome<G> chromosome) {
        return new Individual<>(chromosome);
    }

    private Individual<G, F> wrapAndEvaluate(Chromosome<G> chromosome) {
        return new Individual<>(chromosome, fitnessFunction.apply(chromosome));
    }

    public F getFitness(int index) {
        return individuals.get(index).getFitness();
    }

    public int getSize() {
        return individuals.size();
    }

    public FitnessFunction<G, ? extends F> getFitnessFunction() {
        return fitnessFunction;
    }

    @Override
    public String toString() {
        return individuals.toString();
    }
}
