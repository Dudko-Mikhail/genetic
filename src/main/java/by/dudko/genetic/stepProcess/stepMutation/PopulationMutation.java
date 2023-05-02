package by.dudko.genetic.stepProcess.stepMutation;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.RandomUtils;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public interface PopulationMutation<T> extends UnaryOperator<List<Chromosome<T>>> { // todo подумать над изменением списка хромосом на Популяцию.
    static <T> PopulationMutation<T> of(RandomGenerator random, double chromosomeMutationProbability,
                                        double geneMutationProbability, UnaryOperator<Gene<T>> mutagen) { // todo подумать об этих реализациях (шаблонных методах) и удалением классов реализаций
        return PopulationMutation.of(random, chromosomeMutationProbability,
                (chromosome -> {
                    RandomUtils.randomIndexes(random, 0, chromosome.length(), geneMutationProbability)
                            .forEach(index -> {
                                Gene<T> gene = chromosome.getGene(index);
                                chromosome.replaceGene(index, mutagen.apply(gene));
                            });
                    return chromosome;
                }));
    }

    static <T> PopulationMutation<T> of(RandomGenerator random, double chromosomeMutationProbability,
                                        UnaryOperator<Chromosome<T>> chromosomeMutator) {
        return (chromosomes -> {
            RandomUtils.randomIndexes(random, 0, chromosomes.size(), chromosomeMutationProbability)
                    .forEach(index -> {
                        Chromosome<T> chromosome = chromosomes.get(index);
                        chromosomes.set(index, chromosomeMutator.apply(chromosome));
                    });
            return chromosomes;
        });
    }
}
