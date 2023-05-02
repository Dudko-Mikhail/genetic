package by.dudko.genetic.example;

import by.dudko.genetic.model.chromosome.impl.GenericChromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.evaluation.FitnessFunction;
import by.dudko.genetic.process.initialization.Initializer;

import java.util.Random;

public class MinMaxTask {
    public static void main(String[] args) {
        Random random = new Random();
        Initializer<Boolean> initializer = () -> new GenericChromosome<>(
                new Gene<>(random.nextBoolean()));

        FitnessFunction<Boolean, Integer> fitnessFunction = (chromosome) ->
                chromosome.getGenes()
                    .stream()
                    .mapToInt(gene -> gene.getValue() ? 1 : 0)
                    .reduce(Integer::sum)
                    .orElse(0);

    }
}
