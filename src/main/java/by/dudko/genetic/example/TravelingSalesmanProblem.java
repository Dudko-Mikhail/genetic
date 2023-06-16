package by.dudko.genetic.example;

import by.dudko.genetic.algorithm.GeneticAlgorithm;
import by.dudko.genetic.example.salesman.DataReader;
import by.dudko.genetic.example.salesman.GreedyAlgorithm;
import by.dudko.genetic.example.salesman.LocalSearch;
import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.chromosome.impl.BaseChromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.model.gene.IntegerGene;
import by.dudko.genetic.process.crossover.PopulationCrossover;
import by.dudko.genetic.process.crossover.impl.CycleCrossover;
import by.dudko.genetic.process.crossover.impl.OrderedCrossover;
import by.dudko.genetic.process.evaluation.FitnessFunction;
import by.dudko.genetic.process.initialization.ChromosomeBasedInitializer;
import by.dudko.genetic.process.initialization.Initializer;
import by.dudko.genetic.process.mutation.PopulationMutation;
import by.dudko.genetic.process.mutation.impl.ShuffleMutation;
import by.dudko.genetic.process.replacement.Replacement;
import by.dudko.genetic.process.replacement.impl.JoinReplacement;
import by.dudko.genetic.process.selection.Selection;
import by.dudko.genetic.process.selection.impl.EliteSelection;
import by.dudko.genetic.process.selection.impl.MonteCarloSelection;
import by.dudko.genetic.process.selection.impl.SimpleRankSelection;
import by.dudko.genetic.process.selection.impl.StochasticUniversalSamplingDecorator;
import by.dudko.genetic.statistics.Statistics;
import by.dudko.genetic.util.IndexSelectors;
import by.dudko.genetic.util.RandomUtils;

import java.util.*;
import java.util.stream.IntStream;

public class TravelingSalesmanProblem {
    public static final Random random = new Random();
    public static final int LOG_PERIOD = 1000;
    public static final int GENERATIONS_NUMBER = 1000000;
    public static final int OPT_PERIOD = 1000;

    public static final int DIMENSION = 34;
    public static final String FILENAME = "ALL_atsp/ftv33.atsp";
    public static final long OPTIMAL = 1286;

    public static final int MERGES_LIMIT = 1;
    public static LocalSearch search;

    public static int[][] generateRandomGraphMatrix(int size, int minEdgeLength, int maxEdgeLength) {
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i < j) {
                    matrix[i][j] = matrix[j][i] = random.nextInt(minEdgeLength, maxEdgeLength);
                }
            }
        }
        return matrix;
    }

    public static long calculateRouteLength(Integer[] route, int[][] graph) { // 2281
        long routeLength = 0;
        int endIndex = route.length - 1;
        for (int i = 0; i < endIndex; i++) {
            routeLength += graph[route[i]][route[i + 1]];
        }
        routeLength += graph[route[0]][route[endIndex]];
        return routeLength;
    }

    public static void main(String[] args) throws Exception {
        DataReader reader = new DataReader(FILENAME);
//        int[][] graph = generateRandomGraphMatrix(DIMENSION, 10, 10000);
        int[][] graph = reader.readGraph(DIMENSION);
        search = new LocalSearch(graph);

        int inf = 200;
//        int[][] graph = new int[][]{
//                {inf, 20, 18, 12, 8},
//                {5, inf, 14, 7, 11},
//                {12, 18, inf, 6, 11},
//                {11, 17, 11, inf, 12},
//                {5, 5, 5, 5, inf}
//        };


        Initializer<IntegerGene> routeInitializer = new ChromosomeBasedInitializer<>(
                () -> new BaseChromosome<>(RandomUtils.uniqueRandomNumbers(random, 0, graph.length, graph.length)
                        .mapToObj(IntegerGene::new)
                        .toList()));
        Comparator<Long> fitnessComparator = Comparator.reverseOrder();
        GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm(graph);


//        GeneticAlgorithm<IntegerGene, Long> geneticAlgorithm = buildAlgorithm(graph);
        GeneticAlgorithm<IntegerGene, Long> geneticAlgorithm = buildAlgorithm(graph);

        Integer[] greedySolution = mapToInteger(greedyAlgorithm.buildRoute());
        Integer[] searchSolution = mapToInteger(search.optimize(mapToInt(greedySolution)));
        Chromosome<IntegerGene> bestChromosome = new BaseChromosome<>(
                Arrays.stream(greedySolution)
                        .map(IntegerGene::new)
                        .toList()
        );
        var initPopulation = new ArrayList<>(routeInitializer.produceChromosomes(100)
                .toList());
        initPopulation.set(0, bestChromosome);
        initPopulation.set(20, bestChromosome);

        geneticAlgorithm.runAlgorithm(GENERATIONS_NUMBER);
//        geneticAlgorithm.getStatistics().printTimeStatistics();

        var fittest = geneticAlgorithm.getBestIndividual();
//        System.out.println("Fittest: " + fittest);
        Integer[] route = fittest.getGenes().stream()
                .map(Gene::getValue)
                .toArray(Integer[]::new);

        System.out.println(fittest.getValues(Integer.class));
        System.out.println("Greedy " + calculateRouteLength(greedySolution, graph));
        System.out.println("Genetic: " + calculateRouteLength(route, graph));
        System.out.println("Search " + calculateRouteLength(mapToInteger(search.optimize(mapToInt(greedySolution))), graph));
        System.out.println("Genetic + search: " + calculateRouteLength(mapToInteger(search.optimize(mapToInt(route))), graph));
        System.out.println(geneticAlgorithm.getGenerationNumber());
        geneticAlgorithm.getStatistics().printTimeStatistics();
    }

    public static GeneticAlgorithm<IntegerGene, Long> buildAlgorithm(int[][] graph) {
        var builder = GeneticAlgorithm.<IntegerGene, Long>builder();
        Comparator<Long> fitnessComparator = Comparator.reverseOrder();

        Initializer<IntegerGene> routeInitializer = new ChromosomeBasedInitializer<>(
                () -> new BaseChromosome<>(
                        RandomUtils.uniqueRandomNumbers(random, 0, graph.length, graph.length)
                                .mapToObj(IntegerGene::new)
                                .toList()));

//        Selection<IntegerGene, Long> selection = new StochasticUniversalSamplingDecorator<IntegerGene, Long>(random, 5, new SimpleRankSelection<>(random, fitnessComparator));
        Selection<IntegerGene, Long> selection = new MonteCarloSelection<>(random);

        PopulationCrossover<IntegerGene, Long> crossover = new OrderedCrossover<>(random);
        PopulationCrossover<IntegerGene, Long> cycleCrossover = new CycleCrossover<>(random);

        PopulationMutation<IntegerGene, Long> mutation = PopulationMutation.fromChromosomeMutator(IndexSelectors.probabilitySelector(random, 5.0 / DIMENSION),
                new ShuffleMutation<IntegerGene>());

        Replacement<IntegerGene, Long> replacement = new JoinReplacement<>(new EliteSelection<IntegerGene, Long>(random, 0.1, fitnessComparator));

        Statistics<IntegerGene, Long> statistics = new Statistics<>(LOG_PERIOD);

        statistics.registerMetric("min", population -> population.getIndividuals().stream()
                .map(Individual::getFitness)
                .reduce(Math::min).get(), Long.class);

        statistics.registerMetric("unique", population -> new HashSet<>(population.getIndividuals()).size(),
                Integer.class);
//
//
//        statistics.registerMetric("max", population -> population.getIndividuals().stream()
//                .map(Individual::getFitness)
//                .reduce(Math::max).get(), Long.class);

        builder.statistics(statistics)
                .initializer(routeInitializer)
                .selection(selection)
                .populationCrossover(crossover)
                .populationMutation(mutation)
                .replacement(replacement)
                .fitnessFunction(getFitnessFunction(graph))
                .fitnessComparator(fitnessComparator)
                .populationSize(100)
                .offspringSize(100)
                .populationSizeAfterReplacement(70)
                .statistics(statistics)
                .populationCompletionActive(true)
                .selectedPopulationSize(60)

//                .stopFunction(population -> new HashSet<>(population.getIndividuals()).size() == 1);
                .stopFunction(population -> population.getFittest(fitnessComparator).get().getFitness() == OPTIMAL);


        var firstGA = builder.build();

        var indexSelector = IndexSelectors.<Chromosome<IntegerGene>>fixedSizeSelector(random, 2);
        PopulationMutation<IntegerGene, Long> twoOptMutation = PopulationMutation.fromChromosomeMutator(
                indexSelector, chromsome -> {
                    var route = chromsome.getValues(Integer.class).toArray(Integer[]::new);
                    var optimizedRoute = search.optimize(mapToInt(route));
                    var genes = Arrays.stream(optimizedRoute)
                            .mapToObj(IntegerGene::new).toList();
                    return chromsome.newInstance(genes);
                });

//        firstGA.getEvolutionptors().addMutationListener((ga, ignored) -> {
//            if (ga.getGenerationNumber() % OPT_PERIOD == 0) {
//                twoOptMutation.apply(ga.getCurrentPopulation());
//            }
//        });

        return builder.build();
    }

//    public static ParallelGeneticAlgorithm<IntegerGene, Long> buildParallelGeneticAlgorithm(int[][] graph) {
//        var geneticAlgorithm = buildAlgorithm(graph);
//
//        List<GeneticAlgorithm<IntegerGene, Long>> algorithms = new ArrayList<>(
//                List.of(firstGA, builder.build(),
//                        builder.build(),
//                        builder.build(),
//                        builder.build()));
//
//        ParallelGeneticAlgorithm<IntegerGene, Long> parallelGA = new ParallelGeneticAlgorithm<>(algorithms, builder.build(), MERGES_LIMIT, GENERATIONS_NUMBER / 10,
//                false, Executors.newFixedThreadPool(algorithms.size()));
//    }

    public static FitnessFunction<IntegerGene, Long> getFitnessFunction(int[][] graph) {
        return chromosome -> {
            Integer[] arr = chromosome.getGenes().stream()
                    .map(Gene::getValue)
                    .toArray(Integer[]::new);
            return calculateRouteLength(arr, graph);
        };
    }


    public static int[] mapToInt(Integer[] integerArray) {
        int[] arr = new int[integerArray.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = integerArray[i];
        }
        return arr;
    }

    public static Integer[] mapToInteger(int[] ints) {
        return IntStream.of(ints)
                .mapToObj(Integer::valueOf)
                .toArray(Integer[]::new);
    }
}
