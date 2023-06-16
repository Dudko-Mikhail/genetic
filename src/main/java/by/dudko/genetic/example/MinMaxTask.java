package by.dudko.genetic.example;

import by.dudko.genetic.algorithm.GeneticAlgorithm;
import by.dudko.genetic.builder.mutation.GeneMutationFactory;
import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.BooleanGene;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.crossover.PopulationCrossover;
import by.dudko.genetic.process.crossover.impl.UniformCrossover;
import by.dudko.genetic.process.evaluation.FitnessFunction;
import by.dudko.genetic.process.initialization.ChromosomeBasedInitializer;
import by.dudko.genetic.process.initialization.GeneBasedInitializer;
import by.dudko.genetic.process.initialization.Initializer;
import by.dudko.genetic.process.mutation.PopulationMutation;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.replacement.impl.PercentageReplacement;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.process.selection.impl.EliteSelection;
import by.dudko.genetic.process.selection.impl.TournamentSelection;
import by.dudko.genetic.statistics.Statistics;
import by.dudko.genetic.util.IndexSelectors;

import java.util.Random;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class MinMaxTask {
    public static void main(String[] args) {
        int answer = 5000;
        Random random = new Random();
        Initializer<BooleanGene> initializer = new ChromosomeBasedInitializer<>(
                new GeneBasedInitializer<>(
                        () -> new BooleanGene(random.nextBoolean()),
                        () -> answer
                )
        );

        Predicate<Population<BooleanGene, Long>> stopFunction = population -> {
            var fittest = population.getFittest(Long::compareTo);
            return fittest.map(individual -> individual.getFitness() == answer)
                    .orElse(false);
        };

        FitnessFunction<BooleanGene, Long> fitnessFunction = (chromosome) ->
                chromosome.getGenes()
                        .stream()
                        .map(BooleanGene::getValue)
                        .filter(value -> value)
                        .count();

        Selection<BooleanGene, Long> selection = new TournamentSelection<>(
                random, Long::compare, 4
        );

        PopulationCrossover<BooleanGene, Long> crossover = new UniformCrossover<>(random); // todo проверить наследование

        PopulationMutation<BooleanGene, Long> mutation = PopulationMutation.fromGeneMutator(
                IndexSelectors.probabilitySelector(random, 1.0 / answer), GeneMutationFactory.<BooleanGene>flipBit());

        Selection<BooleanGene, Long> replacementSelection = new EliteSelection<>(
                random, 0.3, Long::compare
        );

        Replacement<BooleanGene, Long> replacement = new PercentageReplacement<>(0.5, replacementSelection);

        Statistics<BooleanGene, Long> statistics = new Statistics<>(100);

        statistics.registerMetric("avg", population -> population.getIndividuals().stream()
                .map(Individual::getFitness)
                .reduce(Long::sum).get() / (double) population.getSize(), Double.class);


        statistics.registerMetric("min", population -> population.getIndividuals().stream()
                .map(Individual::getFitness)
                .reduce(Math::min).get(), Long.class);


        statistics.registerMetric("max", population -> population.getIndividuals().stream()
                .map(Individual::getFitness)
                .reduce(Math::max).get(), Long.class);

        var algorithmBuilder = GeneticAlgorithm.<BooleanGene, Long>builder()
                .initializer(initializer)
                .selection(selection)
                .populationCrossover(crossover)
                .populationMutation(mutation)
                .replacement(replacement)
                .populationSize(60)
                .offspringSize(60)
                .populationSizeAfterReplacement(60)
                .fitnessFunction(fitnessFunction)
                .populationCompletionActive(false)
                .stopFunction(stopFunction)
                .statistics(statistics)
                .fitnessComparator(Long::compareTo);

        GeneticAlgorithm<BooleanGene, Long> geneticAlgorithm = algorithmBuilder.build();
        geneticAlgorithm.runAlgorithm(1000);
//        geneticAlgorithm.runAlgorithm(20);
//        statistics.printFullStatistics();
//        statistics.printMetricLog("avg");
//        statistics.printFullStatistics();
        statistics.printTimeStatistics();

//        ParallelGeneticAlgorithm<Boolean, Long> parallelGA = new ParallelGeneticAlgorithm<>(
//                () -> algorithmBuilder.build(), 8, 1, 100
//        );
//        var lastGeneration = parallelGA.runAlgorithm();
//        printResult(lastGeneration, parallelGA.getBigOne().getGenerationNumber());
//        System.out.println("Best " + lastGeneration.getFittest(Long::compare).get().getFitness());
    }

    static <G extends Gene<?, G>, F> void printResult(Population<G, F> population, long generationNumber) {
        population.getIndividuals()
                .forEach(ind -> System.out.println("Fitness: " + ind.getFitness()));
        System.out.println("Generation number: " + generationNumber);

    }
}