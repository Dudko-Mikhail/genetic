package by.dudko.genetic.algorithm;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/* todo реализовать на базе CompletableFuture. После выполнения алгоритма ложить результат в очередь. Забирать результаты
    через take. В случае возникновения исключения CompletableFuture должен положить null в очередь. Обработчик очереди
    должен это учесть и больше не запускать испорченную версию. Такая стратегия нужна, чтобы предотвратить зависание в методе take
*/
public class ParallelGeneticAlgorithm<G extends Gene<?, G>, F> {
    private final CompletionService<GeneticAlgorithm<G, F>> completionService;
    private List<GeneticAlgorithm<G, F>> algorithms = new ArrayList<>();
    private GeneticAlgorithm<G, F> bigOne;
    private int bigMergesLimit;
    private int generationsBeforeMerge;
    private boolean resetAfterBigMerge;

    public ParallelGeneticAlgorithm(Supplier<GeneticAlgorithm<G, F>> algorithmSupplier,
                                    int count, int bigMergesLimit, int generationsBeforeMerge) {
        this(algorithmSupplier, algorithmSupplier.get(), count, bigMergesLimit, generationsBeforeMerge,
                Executors.newFixedThreadPool(count));
    }

    public ParallelGeneticAlgorithm(Supplier<GeneticAlgorithm<G, F>> algorithmSupplier, GeneticAlgorithm<G, F> bigOne,
                                    int count, int bigMergesLimit, int generationsBeforeMerge,
                                    ExecutorService executorService) {
        RequireUtils.positive(count);
        Objects.requireNonNull(algorithmSupplier);
        for (int i = 0; i < count; i++) {
            algorithms.add(algorithmSupplier.get());
        }
        this.bigOne = bigOne;
        this.bigMergesLimit = bigMergesLimit;
        this.generationsBeforeMerge = generationsBeforeMerge;
        this.completionService = new ExecutorCompletionService<>(executorService);
    }

    public ParallelGeneticAlgorithm(List<GeneticAlgorithm<G, F>> algorithms, GeneticAlgorithm<G, F> bigOne,
                                    int bigMergesLimit, int generationsBeforeMerge, boolean resetAfterBigMerge,
                                    ExecutorService executorService) {
        this.algorithms = new ArrayList<>(algorithms);
        this.bigOne = bigOne;
        this.bigMergesLimit = bigMergesLimit;
        this.resetAfterBigMerge = resetAfterBigMerge;
        this.generationsBeforeMerge = generationsBeforeMerge;
        this.completionService = new ExecutorCompletionService<>(executorService);
    }

    public Population<G, F> runAlgorithm() throws Exception {
        for (int i = 0; i < bigMergesLimit; i++) {
            System.out.println("Before " + generationsBeforeMerge);
            algorithms.forEach(algorithm -> completionService.submit(wrap(algorithm, generationsBeforeMerge)));
            List<Individual<G, F>> individuals = new ArrayList<>();
            for (int j = 0; j < algorithms.size(); j++) {
                completionService.take().get();
//                individuals.addAll(completionService.take().get()
//                        .getCurrentPopulation()
//                        .getIndividuals());
//                algorithms.get(0).setCurrentPopulation(new Population<>(individuals, bigOne.getFitnessFunction()));
                if (resetAfterBigMerge) {
                    for (int k = 1; k < algorithms.size(); k++) {
                        algorithms.get(i).reset();
                    }
                }
            }
            System.out.println("Merge");
            algorithms.forEach(algorithms -> System.out.println("Best fitness: " + algorithms.getBestIndividual()
                    .getFitness()));
        }
        List<Individual<G, F>> individuals = new ArrayList<>(mergeAllAlgorithmsIndividuals());

//        individuals.addAll(bigOne.getCurrentPopulation().getIndividuals());
        return new Population<>(individuals.stream().filter(Objects::nonNull)
                .collect(Collectors.toSet()), bigOne.getFitnessFunction());
    }

    public List<Individual<G, F>> mergeAllAlgorithmsIndividuals() {
        return algorithms.stream()
                .flatMap(algorithm -> algorithm.getCurrentPopulation()
                        .getIndividuals()
                        .stream())
                .toList();
    }

    public Callable<GeneticAlgorithm<G, F>> wrap(GeneticAlgorithm<G, F> geneticAlgorithm, int generationNumber) {
        return () -> {
            System.out.println("Before start");
            geneticAlgorithm.runAlgorithm(generationNumber);
            System.out.println("After run");
            return geneticAlgorithm;
        };
    }

    public GeneticAlgorithm<G, F> getBigOne() {
        return bigOne;
    }

    public List<GeneticAlgorithm<G, F>> getAlgorithms() {
        return List.copyOf(algorithms);
    }
}
