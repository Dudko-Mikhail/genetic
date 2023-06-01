package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BaseGene;

import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public class GeneBasedCrossover<T> implements BinaryOperator<Chromosome<T>> {
    private final RandomGenerator random;
    private final BinaryOperator<BaseGene<T>> geneCrossover;

    public GeneBasedCrossover(RandomGenerator random, BinaryOperator<BaseGene<T>> geneCrossover) {
        this.random = Objects.requireNonNull(random);
        this.geneCrossover = Objects.requireNonNull(geneCrossover);
    }

    @Override
    public final Chromosome<T> apply(Chromosome<T> first, Chromosome<T> second) {
        int newChromosomeLength = Math.min(first.length(), second.length()); // todo почему именно min? Можно решить: вынос в отдельный метод. Еnum параметр в конструкторе
        var genes = Stream.generate(() -> {
                    Map.Entry<BaseGene<T>, BaseGene<T>> selectedGenes = selectGenes(first, second);
                    return geneCrossover.apply(selectedGenes.getKey(), selectedGenes.getValue());
                }).limit(newChromosomeLength)
                .toList();
        return first.newInstance(genes);
    }

    protected Map.Entry<BaseGene<T>, BaseGene<T>> selectGenes(Chromosome<T> first, Chromosome<T> second) { // todo либо интерфейс селектор
        int firstParticipant = random.nextInt(first.length());
        int secondParticipant = random.nextInt(second.length());
        return Map.entry(first.getGene(firstParticipant), second.getGene(secondParticipant));
    }
}
