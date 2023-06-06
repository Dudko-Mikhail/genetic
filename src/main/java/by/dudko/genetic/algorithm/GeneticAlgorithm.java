package by.dudko.genetic.algorithm;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.PopulationCrossover;
import by.dudko.genetic.process.evaluation.FitnessFunction;
import by.dudko.genetic.process.evaluation.StopFunction;
import by.dudko.genetic.process.initialization.Initializer;
import by.dudko.genetic.process.mutation.PopulationMutation;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.statistics.Statistics;
import by.dudko.genetic.statistics.TimeStatistics;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

import static by.dudko.genetic.statistics.TimeStatistics.EvolutionProcess.*;

public class GeneticAlgorithm<G extends Gene<?, G>, F> { // todo add builder; age filter; Fitness Comparator - параметр конструктора?
    protected Initializer<G> initializer;
    protected Selection<G, F> selection;
    protected PopulationCrossover<G> populationCrossover;
    protected PopulationMutation<G> populationMutation;
    protected Replacement<G, F> replacement;
    protected StopFunction<F> stopFunction;
    protected FitnessFunction<G, F> fitnessFunction;
    protected Comparator<? super F> comparator;
    private final Statistics<G, F> statistics;
    private final int populationSize;
    private final int selectedPopulationSize;
    private final int offspringSize;
    private final int populationSizeAfterReplacement;
    private final long chromosomeLiveTime;
    private boolean isPopulationCompletionActive;

    private Population<G, F> currentPopulation;
    private long generationNumber = 1;
    private long oldestChromosomeAge = 1;

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
        this.comparator = builder.comparator;
        this.statistics = builder.statistics;
        this.populationSize = builder.populationSize;
        this.selectedPopulationSize = builder.selectedPopulationSize;
        this.offspringSize = builder.offspringSize;
        this.populationSizeAfterReplacement = builder.populationSizeAfterReplacement;
        this.chromosomeLiveTime = builder.chromosomeLiveTime;
        this.isPopulationCompletionActive = builder.isPopulationCompletionActive;
        if (builder.initialPopulation != null) {
            currentPopulation = builder.initialPopulation;
        }
    }

    public final void runAlgorithm(long generationLimit) {
        if (currentPopulation == null) {
            currentPopulation = initializePopulation();
        }
        TimeStatistics timeStatistics = statistics.getTimeStatistics();
        while (!isTimeToStop(currentPopulation) && generationNumber < generationLimit) {
            timeStatistics.measureTime(EVALUATION, () -> currentPopulation.evaluatePopulation(fitnessFunction));
            statistics.calculateStatistics(currentPopulation, generationNumber);
            Population<G, F> selectedIndividuals = timeStatistics.measureTime(SELECTION,
                    () -> performSelection(currentPopulation));
            Population<G, F> offspring = timeStatistics.measureTime(CROSSOVER,
                    () -> performCrossover(selectedIndividuals));
            Population<G, F> mutatedOffspring = timeStatistics.measureTime(MUTATION, () -> performMutation(offspring));
            currentPopulation = timeStatistics.measureTime(REPLACEMENT,
                    () -> performReplacement(currentPopulation, mutatedOffspring));
//            currentPopulation = performAgeFiltration(currentPopulation);  todo после добавления возраста хромосоме / или начала использования генотипа
            int size = currentPopulation.getSize();
            if (isPopulationCompletionActive && size < populationSize) {
                Stream<Chromosome<G>> newChromosomes = initializer.produceChromosomes(populationSize - size);
                currentPopulation.addAllWithoutEvaluation(newChromosomes.toList());
            }
            updateGenerationNumber();
        }
    }

    public Population<G, F> initializePopulation() {
        return new Population<>(initializer.produceChromosomes(populationSize)
                .toList(), fitnessFunction);
    }

    public Population<G, F> performSelection(Population<G, F> population) {
        return selection.select(population, selectedPopulationSize);
    }

    public Population<G, F> performCrossover(Population<G, F> selectedIndividuals) {
        return (Population<G, F>) populationCrossover.performCrossover(selectedIndividuals, offspringSize);
    }

    public Population<G, F> performMutation(Population<G, F> offspring) {
        return (Population<G, F>) this.populationMutation.apply(offspring);
    }

    public Population<G, F> performReplacement(Population<G, F> oldGeneration, Population<G, F> newGeneration) {
        newGeneration.evaluatePopulation();
        return replacement.replace(oldGeneration, newGeneration, populationSizeAfterReplacement);
    }

//    public Population<G, F> performAgeFiltration(Population<G, F> population) { // todo необходима сущность с данными о поколении рождения
//        var chromosomes = population.getIndividuals();
//        chromosomes.stream()
//                .peek(chromosome -> )
//                .filter(chromosome -> chromosome);
//        return population;
//    }

    boolean isTimeToStop(Population<G, F> population) {
        return stopFunction.test(population);
    }

    public void reset() { // todo change after adding statistics
        currentPopulation = new Population<>(initializer.produceChromosomes(populationSize).toList(), fitnessFunction);
        generationNumber = 1;
    }

    public void updateGenerationNumber() {
        generationNumber++;
    }

    public Statistics<G, F> getStatistics() {
        return statistics;
    }

    public Population<G, F> getCurrentPopulation() {
        return currentPopulation;
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

    public static class GeneticAlgorithmBuilder<G extends Gene<?, G>, F> {
        private Initializer<G> initializer;
        private Population<G, F> initialPopulation;
        private Selection<G, F> selection;
        private PopulationCrossover<G> populationCrossover;
        private PopulationMutation<G> populationMutation;
        private Replacement<G, F> replacement;
        private StopFunction<F> stopFunction;
        private FitnessFunction<G, F> fitnessFunction;
        private Comparator<? super F> comparator;
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

        public GeneticAlgorithmBuilder<G, F> populationCrossover(PopulationCrossover<G> populationCrossover) {
            this.populationCrossover = populationCrossover;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> populationMutation(PopulationMutation<G> populationMutation) {
            this.populationMutation = populationMutation;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> replacement(Replacement<G, F> replacement) {
            this.replacement = replacement;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> stopFunction(StopFunction<F> stopFunction) {
            this.stopFunction = stopFunction;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> fitnessFunction(FitnessFunction<G, F> fitnessFunction) {
            this.fitnessFunction = fitnessFunction;
            return this;
        }

        public GeneticAlgorithmBuilder<G, F> comparator(Comparator<? super F> comparator) {
            this.comparator = comparator;
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