package by.dudko.genetic.model;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.process.evaluation.FitnessFunction;

import java.util.*;

public class Population<T, F> { // todo для вычисления самого приспособленного компаратор можно получать через конструктор
    private final List<Individual<T, F>> individuals;
    private FitnessFunction<T, ? extends F> fitnessFunction;
    private boolean isEvaluated;

    public static <T, F> Comparator<Individual<T, F>> toIndividualsComparator(Comparator<? super F> fitnessComparator) {
        return (a, b) -> fitnessComparator.compare(a.getFitness(), b.getFitness());
    }

    public Population(Collection<? extends Chromosome<T>> chromosomes, FitnessFunction<T, ? extends F> fitnessFunction) {
        this(chromosomes);
        this.fitnessFunction = Objects.requireNonNull(fitnessFunction);
    }

    public Population(Collection<? extends Chromosome<T>> chromosomes) {
        this.individuals = new ArrayList<>(
                chromosomes.stream()
                        .map(this::wrap)
                        .toList()
        );
    }

    public Population(Chromosome<T>... chromosomes) {
        this(Arrays.stream(chromosomes)
                .toList());
    }


    public Population(FitnessFunction<T, F> fitnessFunction, Chromosome<T>... chromosomes) {
        this(Arrays.stream(chromosomes)
                .toList(), fitnessFunction);
    }

    public void evaluatePopulation() { // todo потокобезопасность
        if (isEvaluated) {
            return;
        }
        if (fitnessFunction == null) {
            throw new IllegalStateException("Cannot evaluate population without fitness function");
        }
        individuals.forEach(individual -> {
            if (individual.isEvaluated()) {
                individual.evaluateAndSetFitness(fitnessFunction);
            }
        });
        individuals.stream()
                .filter(Individual::nonEvaluated)
                .forEach(individual -> individual.evaluateAndSetFitness(fitnessFunction));
        isEvaluated = true;
    }

    public void evaluatePopulation(FitnessFunction<T, F> fitnessFunction) { // todo не потокобезопасно
        this.fitnessFunction = Objects.requireNonNull(fitnessFunction);
        individuals.forEach(individual -> individual.evaluateAndSetFitness(fitnessFunction));
        isEvaluated = true;
    }

    public Optional<Individual<T, F>> getFittest(Comparator<? super F> comparator) { // todo можно вычислять одновременно в методе evaluate!!!
        if (nonEvaluated()) {
            evaluatePopulation();
        }
        return individuals.stream()
                .max(toIndividualsComparator(Objects.requireNonNull(comparator)));
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

    public List<Individual<T, F>> getIndividuals() {
        return Collections.unmodifiableList(individuals);
    }

    public void addAllWithoutEvaluation(Collection<? extends Chromosome<T>> chromosomes) {
        individuals.addAll(
                chromosomes.stream()
                        .map(this::wrap)
                        .toList()
        );
        // todo implement. Возраст нужно вынести в хромосому или хранить номер текущего поколения в популяции
    }

    public void addAllWithEvaluation(Collection<? extends Chromosome<T>> chromosomes) {
        individuals.addAll(
                chromosomes.stream()
                        .map(this::wrapAndEvaluate)
                        .toList()
        );
        // todo implement. Возраст нужно вынести в хромосому или хранить номер текущего поколения в популяции
    }

    public Individual<T, F> getIndividual(int index) {
        return individuals.get(index);
    }

    public Individual<T, F> replaceIndividual(int index, Chromosome<T> newChromosome) {
        return individuals.set(index, wrapAndEvaluate(newChromosome));
    }

    private Individual<T, F> wrap(Chromosome<T> chromosome) {
        return new Individual<>(chromosome);
    }

    private Individual<T, F> wrapAndEvaluate(Chromosome<T> chromosome) {
        return new Individual<>(chromosome, fitnessFunction.apply(chromosome));
    }

    public F getFitness(int index) {
        return individuals.get(index).getFitness();
    }

    public int getSize() {
        return individuals.size();
    }

    public FitnessFunction<T, ? extends F> getFitnessFunction() {
        return fitnessFunction;
    }
}
