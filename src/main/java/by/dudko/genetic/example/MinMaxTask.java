package by.dudko.genetic.example;

import by.dudko.genetic.algorithm.GeneticAlgorithm;
import by.dudko.genetic.algorithm.ParallelGeneticAlgorithm;
import by.dudko.genetic.builder.mutation.GeneMutationFactory;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.BaseGene;
import by.dudko.genetic.process.crossover.ChromosomeBasedCrossover;
import by.dudko.genetic.process.crossover.PopulationCrossover;
import by.dudko.genetic.process.crossover.impl.UniformCrossover;
import by.dudko.genetic.process.evaluation.FitnessFunction;
import by.dudko.genetic.process.evaluation.StopFunction;
import by.dudko.genetic.process.initialization.ChromosomeBasedInitializer;
import by.dudko.genetic.process.initialization.GeneBasedInitializer;
import by.dudko.genetic.process.initialization.Initializer;
import by.dudko.genetic.process.mutation.ChromosomeBasedMutation;
import by.dudko.genetic.process.mutation.GeneBasedMutation;
import by.dudko.genetic.process.mutation.PopulationMutation;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.replacement.impl.PercentageReplacement;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.process.selection.impl.EliteSelection;
import by.dudko.genetic.process.selection.impl.TournamentSelection;
import by.dudko.genetic.util.SelectorsFactory;

import java.util.Random;

public class MinMaxTask {
    public static void main(String[] args) { // todo implement task
        int answer = 1000;
        Random random = new Random();
        Initializer<Boolean> initializer = new ChromosomeBasedInitializer<>(
                new GeneBasedInitializer<>(
                        () -> new BaseGene<>(random.nextBoolean()),
                        () -> answer
                )
        );


        // todo Стоп функция. Необязательно список стоп-функций: можно цепочку (andThen...)
        StopFunction<Long> stopFunction = population -> {
            var fittest = population.getFittest(Long::compareTo);
            return fittest
                    .map(individual -> individual.getFitness() == answer)
                    .orElse(false);
        };

        FitnessFunction<Boolean, Long> fitnessFunction = (chromosome) ->
                chromosome.getGenes()
                        .stream()
                        .map(BaseGene::getValue)
                        .filter(value -> value)
                        .count();

        Selection<Boolean, Long> selection = new TournamentSelection<>(
                random, Long::compare, 4
        );

        PopulationCrossover<Boolean> crossover = new ChromosomeBasedCrossover<>(
                random, new UniformCrossover<>(random)
        );

        PopulationMutation<Boolean> mutation = new ChromosomeBasedMutation<>(
                SelectorsFactory.probabilitySelector(random, 1),
                new GeneBasedMutation<>(SelectorsFactory.probabilitySelector(random, 0.001),
                        GeneMutationFactory.flipBit())
        );

        Selection<Boolean, Long> replacementSelection = new EliteSelection<>(
                random, 0.3, Long::compare
        );

        Replacement<Boolean, Long> replacement = new PercentageReplacement<>(0.5, replacementSelection);

        var algorithmBuilder = GeneticAlgorithm.<Boolean, Long>builder()
                .initializer(initializer)
                .selection(selection)
                .populationCrossover(crossover)
                .populationMutation(mutation)
                .replacement(replacement)
                .populationSize(100)
                .offspringSize(30)
                .populationSizeAfterReplacement(40)
                .fitnessFunction(fitnessFunction)
                .populationCompletionActive(true)
                .stopFunction(stopFunction)
                .comparator(Long::compareTo);

        GeneticAlgorithm<Boolean, Long> geneticAlgorithm = algorithmBuilder.build();
        geneticAlgorithm.runAlgorithm(1000); // todo количество запусков в параметр метода
        var resultPopulation = geneticAlgorithm.getCurrentPopulation();
        printResult(resultPopulation, geneticAlgorithm.getGenerationNumber());
        System.out.println("Best " + resultPopulation.getFittest(Long::compare).get().getFitness());


//        ParallelGeneticAlgorithm<Boolean, Long> parallelGA = new ParallelGeneticAlgorithm<>(
//                () -> algorithmBuilder.build(), 8, 1, 100
//        );
//        var lastGeneration = parallelGA.runAlgorithm();
//        printResult(lastGeneration, parallelGA.getBigOne().getGenerationNumber());
//        System.out.println("Best " + lastGeneration.getFittest(Long::compare).get().getFitness());
    }

    static <T, F> void printResult(Population<T, F> population, long generationNumber) {
        population.getIndividuals()
                .forEach(ind -> System.out.println("Fitness: " + ind.getFitness()));
        System.out.println("Generation number: " + generationNumber);

    }
}