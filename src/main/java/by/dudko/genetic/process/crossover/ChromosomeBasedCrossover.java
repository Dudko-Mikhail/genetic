package by.dudko.genetic.process.crossover;

import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.util.RequireUtils;

import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public class ChromosomeBasedCrossover<T> implements PopulationCrossover<T> {
    private final RandomGenerator random;
    private final BinaryOperator<Chromosome<T>> chromosomeCrossover;

    public ChromosomeBasedCrossover(RandomGenerator random, BinaryOperator<Chromosome<T>> chromosomeCrossover) { // todo рандом параметр селекции
        this.random = Objects.requireNonNull(random);
        this.chromosomeCrossover = Objects.requireNonNull(chromosomeCrossover);
    }

    @Override
    public final Population<T, ?> performCrossover(Population<T, ?> population, int newGenerationSize) {
        int size = population.getSize();
        RequireUtils.greater(size, 1);

        var chromosomes = Stream.generate(() -> {
                    Map.Entry<Chromosome<T>, Chromosome<T>> selectedChromosomes = selectChromosomes(population);
                    return chromosomeCrossover.apply(selectedChromosomes.getKey(),
                            selectedChromosomes.getValue());
                })
                .limit(newGenerationSize)
                .toList();
        return new Population<>(chromosomes, population.getFitnessFunction());
    }

    protected Map.Entry<Chromosome<T>, Chromosome<T>> selectChromosomes(Population<T, ?> population) { // todo или вынести в отдельный интерфейс
        int size = population.getSize();
        int firstParticipant = random.nextInt(size);
        int secondParticipant;
        do {
            secondParticipant = random.nextInt(size);
        } while (firstParticipant == secondParticipant);
        return Map.entry(population.getIndividual(firstParticipant), population.getIndividual(secondParticipant));
    }

    public BinaryOperator<Chromosome<T>> getChromosomeCrossover() {
        return chromosomeCrossover;
    }
}
