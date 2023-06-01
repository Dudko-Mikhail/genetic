package by.dudko.genetic.algorithm;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.process.crossover.PopulationCrossover;
import by.dudko.genetic.process.evaluation.FitnessFunction;
import by.dudko.genetic.process.evaluation.StopFunction;
import by.dudko.genetic.process.initialization.Initializer;
import by.dudko.genetic.process.mutation.PopulationMutation;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.selection.Selection;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

public class GeneticAlgorithm<T, F> { // todo add builder; age filter; Fitness Comparator - параметр конструктора?
    protected Initializer<T> initializer;
    protected Selection<T, F> selection;
    protected PopulationCrossover<T> populationCrossover;
    protected PopulationMutation<T> populationMutation;
    protected Replacement<T, F> replacement;
    protected StopFunction<F> stopFunction;
    protected FitnessFunction<T, F> fitnessFunction;
    protected Comparator<? super F> comparator;
    private final int populationSize;
    private final int selectedPopulationSize;
    private final int offspringSize;
    private final int populationSizeAfterReplacement;
    private final long chromosomeLiveTime;
    private boolean isPopulationCompletionActive;

    private Population<T, F> currentPopulation;
    private long generationNumber = 1;
    private long oldestChromosomeAge = 1;

    public static <T, F> GeneticAlgorithmBuilder<T, F> builder() {
        return new GeneticAlgorithmBuilder<>();
    }

    private GeneticAlgorithm(GeneticAlgorithmBuilder<T, F> builder) {
        this.initializer = builder.initializer;
        this.selection = builder.selection;
        this.populationCrossover = builder.populationCrossover;
        this.populationMutation = builder.populationMutation;
        this.replacement = builder.replacement;
        this.stopFunction = builder.stopFunction;
        this.fitnessFunction = builder.fitnessFunction;
        this.comparator = builder.comparator;
        this.populationSize = builder.populationSize;
        this.selectedPopulationSize = builder.selectedPopulationSize;
        this.offspringSize = builder.offspringSize;
        this.populationSizeAfterReplacement = builder.populationSizeAfterReplacement;
        this.chromosomeLiveTime = builder.chromosomeLiveTime;
        this.isPopulationCompletionActive = builder.isPopulationCompletionActive;
        var initialPopulation = builder.initialPopulation;
        if (builder.initialPopulation != null) {
            currentPopulation = builder.initialPopulation;
        }
    }

    public final void runAlgorithm(long generationLimit) {
        if (currentPopulation == null) {
            currentPopulation = initializePopulation();
        }
        while (!isTimeToStop(currentPopulation) && generationNumber < generationLimit) {
            currentPopulation.evaluatePopulation(fitnessFunction);
            Population<T, F> selectedIndividuals = performSelection(currentPopulation);
            Population<T, F> offspring = performCrossover(selectedIndividuals);
            Population<T, F> mutatedOffspring = performMutation(offspring);
            currentPopulation = performReplacement(currentPopulation, mutatedOffspring);
//            currentPopulation = performAgeFiltration(currentPopulation);  todo после добавления возраста хромосоме / или начала использования генотипа
            int size = currentPopulation.getSize();
            if (isPopulationCompletionActive && size < populationSize) {
                Stream<Chromosome<T>> newChromosomes = initializer.produceChromosomes(populationSize - size);
                currentPopulation.addAllWithoutEvaluation(newChromosomes.toList());
            }
            updateGenerationNumber();
            if (generationNumber % 10 == 0) { // todo remove
                System.out.println("Generation number [%d]".formatted(getGenerationNumber()));
                System.out.println("Fittest [%d]".formatted(currentPopulation.getFittest(comparator).get().getFitness()));
            }
        }
    }

    public Population<T, F> initializePopulation() {
        return new Population<>(initializer.produceChromosomes(populationSize)
                .toList(), fitnessFunction);
    }

    public Population<T, F> performSelection(Population<T, F> population) {
        return selection.select(population, selectedPopulationSize);
    }

    public Population<T, F> performCrossover(Population<T, F> selectedIndividuals) {
        return (Population<T, F>) populationCrossover.performCrossover(selectedIndividuals, offspringSize);
    }

    public Population<T, F> performMutation(Population<T, F> offspring) {
        return (Population<T, F>) this.populationMutation.apply(offspring);
    }

    public Population<T, F> performReplacement(Population<T, F> oldGeneration, Population<T, F> newGeneration) {
        newGeneration.evaluatePopulation();
        return replacement.replace(oldGeneration, newGeneration, populationSizeAfterReplacement);
    }

//    public Population<T, F> performAgeFiltration(Population<T, F> population) { // todo необходима сущность с данными о поколении рождения
//        var chromosomes = population.getIndividuals();
//        chromosomes.stream()
//                .peek(chromosome -> )
//                .filter(chromosome -> chromosome);
//        return population;
//    }

    boolean isTimeToStop(Population<T, F> population) {
        return stopFunction.test(population);
    }

    public void reset() { // todo change after adding statistics
        currentPopulation = new Population<>(initializer.produceChromosomes(populationSize).toList(), fitnessFunction);
        generationNumber = 1;
    }

    public void updateGenerationNumber() {
        generationNumber++;
    }

    public Population<T, F> getCurrentPopulation() {
        return currentPopulation;
    }

    public long getGenerationNumber() {
        return generationNumber;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setCurrentPopulation(Population<T, F> population) {
        this.currentPopulation = population;
    }

    public static class GeneticAlgorithmBuilder<T, F> {
        private Initializer<T> initializer;
        private Population<T, F> initialPopulation;
        private Selection<T, F> selection;
        private PopulationCrossover<T> populationCrossover;
        private PopulationMutation<T> populationMutation;
        private Replacement<T, F> replacement;
        private StopFunction<F> stopFunction;
        private FitnessFunction<T, F> fitnessFunction;
        private Comparator<? super F> comparator;
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

        public GeneticAlgorithm<T, F> build() { // todo валидация данных либо здесь, либо в сеттерах
            return new GeneticAlgorithm<>(this);
        }

        public GeneticAlgorithmBuilder<T, F> initializer(Initializer<T> initializer) {
            this.initializer = initializer;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> initialPopulation(Collection<? extends Chromosome<T>> chromosomes) {
            this.initialPopulation = new Population<>(chromosomes);
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> selection(Selection<T, F> selection) {
            this.selection = selection;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> populationCrossover(PopulationCrossover<T> populationCrossover) {
            this.populationCrossover = populationCrossover;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> populationMutation(PopulationMutation<T> populationMutation) {
            this.populationMutation = populationMutation;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> replacement(Replacement<T, F> replacement) {
            this.replacement = replacement;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> stopFunction(StopFunction<F> stopFunction) {
            this.stopFunction = stopFunction;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> fitnessFunction(FitnessFunction<T, F> fitnessFunction) {
            this.fitnessFunction = fitnessFunction;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> comparator(Comparator<? super F> comparator) {
            this.comparator = comparator;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> populationSize(int populationSize) {
            this.populationSize = populationSize;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> selectedPopulationSize(int selectedPopulationSize) {
            this.selectedPopulationSize = selectedPopulationSize;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> populationSizeAfterReplacement(int populationSizeAfterReplacement) {
            this.populationSizeAfterReplacement = populationSizeAfterReplacement;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> offspringSize(int offspringSize) {
            this.offspringSize = offspringSize;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> chromosomeLiveTime(long chromosomeLiveTime) {
            this.chromosomeLiveTime = chromosomeLiveTime;
            return this;
        }

        public GeneticAlgorithmBuilder<T, F> populationCompletionActive(boolean populationCompletionActive) {
            isPopulationCompletionActive = populationCompletionActive;
            return this;
        }
    }
}