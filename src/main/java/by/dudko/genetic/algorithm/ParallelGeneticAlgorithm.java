package by.dudko.genetic.algorithm;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.util.RequireUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Supplier;

public class ParallelGeneticAlgorithm<T, F> {
    private List<GeneticAlgorithm<T, F>> algorithms = new ArrayList<>();
    private GeneticAlgorithm<T, F> bigOne;
    private CyclicBarrier barrier;
    private int currentMerge;
    private int bigMergesLimit;
    private int generationsBeforeMerge;

    public ParallelGeneticAlgorithm(Supplier<GeneticAlgorithm<T, F>> algorithmSupplier,
                                    int count, int bigMergesLimit, int generationsBeforeMerge) {
        this(algorithmSupplier, algorithmSupplier.get(), count, bigMergesLimit, generationsBeforeMerge);
    }

    public ParallelGeneticAlgorithm(Supplier<GeneticAlgorithm<T, F>> algorithmSupplier, GeneticAlgorithm<T, F> bigOne,
                                    int count, int bigMergesLimit, int generationsBeforeMerge) {
        RequireUtils.positive(count);
        Objects.requireNonNull(algorithmSupplier);
        barrier = new CyclicBarrier(count);
        for (int i = 0; i < count; i++) {
            algorithms.add(algorithmSupplier.get());
        }
        this.bigOne = bigOne;
        this.bigMergesLimit = bigMergesLimit;
        this.generationsBeforeMerge = generationsBeforeMerge;
    }

    public Population<T, F> runAlgorithm() {
        ConcurrentLinkedDeque<Individual<T, F>> individuals = new ConcurrentLinkedDeque<>();
        Thread[] threads = new Thread[algorithms.size()];
        for (int i = 0; i < bigMergesLimit; i++) {
            for (int j = 0; j < algorithms.size(); j++) {
                var algorithm = algorithms.get(j);
                Runnable runnable = () -> {
                    algorithm.runAlgorithm(generationsBeforeMerge);
                    individuals.addAll(algorithm.getCurrentPopulation().getIndividuals());
                    algorithm.reset();
                };
                threads[j] = new Thread(runnable);
                threads[j].start();
            }
            for (var thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.out.println("========= Something bad happened ===========");
                }
            }

            System.out.println("Before merge Size " + individuals.size());
            bigOne.setCurrentPopulation(new Population<>(individuals, bigOne.fitnessFunction));
            bigOne.runAlgorithm(generationsBeforeMerge);
            return bigOne.getCurrentPopulation();
        }
        return null;
    }

    public GeneticAlgorithm<T, F> getBigOne() {
        return bigOne;
    }
}