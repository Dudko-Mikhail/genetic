package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;

import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public class GeneBasedCrossover<G extends Gene<?, G>> implements BinaryOperator<Chromosome<G>> {
    private final RandomGenerator random;
    private final BinaryOperator<G> geneCrossover;

    public GeneBasedCrossover(RandomGenerator random, BinaryOperator<G> geneCrossover) {
        this.random = Objects.requireNonNull(random);
        this.geneCrossover = Objects.requireNonNull(geneCrossover);
    }

    @Override
    public final Chromosome<G> apply(Chromosome<G> first, Chromosome<G> second) {
        int newChromosomeLength = Math.min(first.length(), second.length()); // todo почему именно min? Можно решить: вынос в отдельный метод. Еnum параметр в конструкторе
        var genes = Stream.generate(() -> {
                    Map.Entry<G, G> selectedGenes = selectGenes(first, second);
                    return geneCrossover.apply(selectedGenes.getKey(), selectedGenes.getValue());
                }).limit(newChromosomeLength)
                .toList();
        return first.newInstance(genes);
    }

    protected Map.Entry<G, G> selectGenes(Chromosome<G> first, Chromosome<G> second) { // todo либо интерфейс селектор
        int firstParticipant = random.nextInt(first.length());
        int secondParticipant = random.nextInt(second.length());
        return Map.entry(first.getGene(firstParticipant), second.getGene(secondParticipant));
    }
}
