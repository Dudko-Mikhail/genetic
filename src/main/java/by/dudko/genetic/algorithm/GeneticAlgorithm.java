package by.dudko.genetic.algorithm;

import by.dudko.genetic.algorithm.listeners.EvolutionInterceptors;
import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.PopulationCrossover;
import by.dudko.genetic.process.evaluation.FitnessFunction;
import by.dudko.genetic.process.initialization.Initializer;
import by.dudko.genetic.process.mutation.PopulationMutation;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.statistics.Statistics;
import by.dudko.genetic.statistics.TimeStatistics;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static by.dudko.genetic.statistics.TimeStatistics.MeasuredProcesses.*;

public class GeneticAlgorithm<G extends Gene<?, G>, F> { // todo add builder; age filter;
    private Initializer<G> initializer;
    private Selection<G, F> selection;
    private PopulationCrossover<G, F> populationCrossover;
    private PopulationMutation<G, F> populationMutation;
    private Replacement<G, F> replacement;
    private final Predicate<Population<G, F>> stopFunction;
    private final EvolutionInterceptors<G, F> evolutionInterceptors;
    private final FitnessFunction<G, F> fitnessFunction;
    private final Comparator<? super F> fitnessComparator;
    private final Statistics<G, F> statistics;
    private final int populationSize;
    private int selectedPopulationSize;
    private int offspringSize;
    private int populationSizeAfterReplacement;
    private boolean isPopulationCompletionActive;
    private Population<G, F> currentPopulation;
    private long generationNumber;

    private long oldestChromosomeAge;
    private final long chromosomeLiveTime;

    public static <G extends Gene<?, G>, F> GeneticAlgorithmBuilder<G, F> builder() {
        return new GeneticAlgorithmBuilder<>();
    }

    private GeneticAlgorithm(GeneticAlgorithmBuilder<G, F> builder) {
        this.initializer = builder.initializer;
        this.selection = builder.selection;
        this.populationCrossover = builder.populationCrossover;
        this.populationMutation = builder.populationMutation;
        this.replacement = builder.replacement;
        this.stopFunction = builder.stopFunction;
        this.fitnessFunction = builder.fitnessFunction;
        this.fitnessComparator = builder.fitnessComparator;
        this.statistics =  builder.statistics;
        this.populationSize = builder.populationSize;
        this.selectedPopulationSize = builder.selectedPopulationSize;
        this.offspringSize = builder.offspringSize;
        this.populationSizeAfterReplacement = builder.populationSizeAfterReplacement;
        this.chromosomeLiveTime = builder.chromosomeLiveTime;
        this.isPopulationCompletionActive = builder.isPopulationCompletionActive;
        if (builder.initialPopulation != null) {
            currentPopulation = builder.initialPopulation;
        }
        evolutionInterceptors = new EvolutionInterceptors<>();
        generationNumber = 1;
    }

    public final void runAlgorithm(long generationLimit) {
        long actualGenerationLimit = generationNumber + generationLimit;
        defineInitialPopulation();
        TimeStatistics timeStatistics = statistics.getTimeStatistics();
        long start = timeStatistics.currentMillis();
        while (continueEvolution(currentPopulation, actualGenerationLimit)) {
            timeStatistics.measureTime(EVALUATION, () -> currentPopulation.evaluatePopulation(fitnessFunction));
            statistics.calculateStatistics(currentPopulation, generationNumber);
            evolutionInterceptors.beforeSelection(this);
            Population<G, F> selectedIndividuals = timeStatistics.measureTime(SELECTION,
                    () -> selection.select(currentPopulation, selectedPopulationSize));

            evolutionInterceptors.beforeCrossover(this, selectedIndividuals);
            Population<G, F> offspring = timeStatistics.measureTime(CROSSOVER,
                    () -> populationCrossover.performCrossover(selectedIndividuals, offspringSize));

            evolutionInterceptors.beforeMutation(this, offspring);
            Population<G, F> mutatedOffspring = timeStatistics.measureTime(MUTATION,
                    () -> populationMutation.apply(offspring));

            timeStatistics.measureTime(EVALUATION, () -> mutatedOffspring.evaluatePopulation(fitnessFunction));
            evolutionInterceptors.beforeReplacement(this, mutatedOffspring);
            currentPopulation = timeStatistics.measureTime(REPLACEMENT,
                    () -> replacement.replace(currentPopulation, mutatedOffspring, populationSizeAfterReplacement));

            int size = currentPopulation.getSize();
            if (isPopulationCompletionActive && size < populationSize) {
                Stream<Chromosome<G>> newChromosomes = initializer.produceChromosomes(populationSize - size);
                currentPopulation.addAllWithoutEvaluation(newChromosomes.toList());
            }
            generationNumber++;
        }
        timeStatistics.acceptTime(ALGORITHM_DURATION, timeStatistics.currentMillis() - start);
    }

    private void defineInitialPopulation() {
        if (currentPopulation == null) {
            currentPopulation = new Population<>(initializer.produceChromosomes(populationSize)
                    .toList(), fitnessFunction);
        }
    }


//    public Population<G, F> performAgeFiltration(Population<G, F> population) { // todo необходима сущность с данными о поколении рождения
//        var chromosomes = population.getIndividuals();
//        chromosomes.stream()
//                .peek(chromosome -> )
//                .filter(chromosome -> chromosome);
//        return population;
//    }

    boolean continueEvolution(Population<G, F> population, long actualGenerationLimit) {
        return generationNumber < actualGenerationLimit && !stopFunction.test(population);
    }

    public void reset() { // todo change after adding statistics
        currentPopulation = new Population<>(initializer.produceChromosomes(populationSize).toList(), fitnessFunction);
        generationNumber = 1;
    }

    public Initializer<G> getInitializer() {
        return initializer;
    }

    public void setInitializer(Initializer<G> initializer) {
        this.initializer = initializer;
    }

    public Selection<G, F> getSelection() {
        return selection;
    }

    public void setSelection(Selection<G, F> selection) {
        this.selection = selection;
    }

    public PopulationCrossover<G, F> getPopulationCrossover() {
        return populationCrossover;
    }

    public void setPopulationCrossover(PopulationCrossover<G, F> populationCrossover) {
        this.populationCrossover = populationCrossover;
    }

    public PopulationMutation<G, F> getPopulationMutation() {
        return populationMutation;
    }

    public void setPopulationMutation(PopulationMutation<G, F> populationMutation) {
        this.populationMutation = populationMutation;
    }

    public Replacement<G, F> getReplacement() {
        return replacement;
    }

    public void setReplacement(Replacement<G, F> replacement) {
        this.replacement = replacement;
    }

    public Predicate<Population<G, F>> getStopFunction() {
        return stopFunction;
    }

    public FitnessFunction<G, F> getFitnessFunction() {
        return fitnessFunction;
    }

    public Comparator<? super F> getFitnessComparator() {
        return fitnessComparator;
    }

    public int getSelectedPopulationSize() {
        return selectedPopulationSize;
    }

    public int getOffspringSize() {
        return offspringSize;
    }

    public int getPopulationSizeAfterReplacement() {
        return populationSizeAfterReplacement;
    }

    public boolean isPopulationCompletionActive() {
        return isPopulationCompletionActive;
    }

    public void setPopulationCompletionActive(boolean populationCompletionActive) {
        isPopulationCompletionActive = populationCompletionActive;
    }

    public long getChromosomeLiveTime() {
        return chromosomeLiveTime;
    }

    public long getOldestChromosomeAge() {
        return oldestChromosomeAge;
    }

    public Statistics<G, F> getStatistics() {
        return statistics;
    }

    public Population<G, F> getCurrentPopulation() {
        return currentPopulation;
    }

    public Individual<G, F> getBestIndividual() {
        return currentPopulation.getFittest(fitnessComparator)
                .orElseThrow();
    }

    public Individual<G, F> getWorstIndividual() {
        return currentPopulation.getFittest(fitnessComparator.reversed())
                .orElseThrow();
    }

    public long getGenerationNumber() {
        return generationNumber;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setCurrentPopulation(Population<G, F> population) {
        this.currentPopulation = population;
    }

    public EvolutionInterceptors<G, F> getEvolutionInterceptors() {
        return evolutionInterceptors;
    }

    public static class GeneticAlgorithmBuilder<G extends Gene<?, G>, F> {
        private Initializer<G> initializer;
        private Population<G, F> initialPopulation;
        private Selection<G, F> selection;
        private PopulationCrossover<G, F> populationCrossover;
        private PopulationMutation<G, F> populationMutation;
        private Replacement<G, F> replacement;
        private Predicate<Population<G, F>> stopFunction;
        private FitnessFunction<G, F> fitnessFunction;
        private Comparator<? super F> fitnessComparator;
        private Statistics<G, F> statistics;
        private int populationSize;
        private int selectedPopulationSize;
        private int offspringSize;
        private int populationSizeAfterReplacement;
        private long chromosomeLiveTime;
        private boolean isPopulationCompletionActive;

        public GeneticAlgorithmBuilder() { // todo optimize default parameters
            populationSize = 50;
            selectedPopulationSize = (int) (0.5 * populationSize);
            offspringSize = populationSize;
            populationSizeAfterReplacement = (int) (0.9 * populationSize);
            isPopulationCompletionActive = true;
            chromosomeLiveTime = 50;
            stopFunction = population -> false;
        }

        public GeneticAlgorithm<G, F> build() { // todo валидация данных либо здесь, либо в сеттерах
            return new GeneticAlgorithm<>(this);
        }

        public GeneticAlgorithmBuilder<G, F> initializer(Initializer<G> initializer) {
            this.initializer = initializer;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> initialPopulation(Collection<? extends Chromosome<G>> chromosomes) {
            this.initialPopulation = new Population<>(chromosomes);
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> selection(Selection<G, F> selection) {
            this.selection = selection;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> populationCrossover(PopulationCrossover<G, F> populationCrossover) {
            this.populationCrossover = populationCrossover;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> populationMutation(PopulationMutation<G, F> populationMutation) {
            this.populationMutation = populationMutation;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> replacement(Replacement<G, F> replacement) {
            this.replacement = replacement;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> stopFunction(Predicate<Population<G, F>> stopFunction) {
            this.stopFunction = stopFunction;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> fitnessFunction(FitnessFunction<G, F> fitnessFunction) {
            this.fitnessFunction = fitnessFunction;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> fitnessComparator(Comparator<? super F> comparator) {
            this.fitnessComparator = comparator;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> statistics(Statistics<G, F> statistics) {
            this.statistics = statistics;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> populationSize(int populationSize) {
            this.populationSize = populationSize;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> selectedPopulationSize(int selectedPopulationSize) {
            this.selectedPopulationSize = selectedPopulationSize;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> populationSizeAfterReplacement(int populationSizeAfterReplacement) {
            this.populationSizeAfterReplacement = populationSizeAfterReplacement;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> offspringSize(int offspringSize) {
            this.offspringSize = offspringSize;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> chromosomeLiveTime(long chromosomeLiveTime) {
            this.chromosomeLiveTime = chromosomeLiveTime;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> populationCompletionActive(boolean populationCompletionActive) {
            isPopulationCompletionActive = populationCompletionActive;
            return this;
        }
    }
}