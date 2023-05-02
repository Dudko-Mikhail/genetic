package by.dudko.genetic.stepProcess.simplegeneralization.impl;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.stepProcess.simplegeneralization.GeneInitializer;
import by.dudko.genetic.stepProcess.simplegeneralization.PopulationInitializer;

import java.util.function.Function;
import java.util.stream.Stream;

public class GeneBasedInitializer<T> implements PopulationInitializer<T> {
    private final int chromosomeLength;
    private final Function<Stream<Gene<T>>, Chromosome<T>> producer;
    private final GeneInitializer<T> geneInitializer;

    public GeneBasedInitializer(int chromosomeLength, Function<Stream<Gene<T>>, Chromosome<T>> producer,
                                GeneInitializer<T> geneInitializer) {
        this.chromosomeLength = chromosomeLength;
        this.producer = producer;
        this.geneInitializer = geneInitializer;
    }

    @Override
    public final Stream<Chromosome<T>> initializePopulation(int populationSize) {
        return Stream.generate(this::createChromosome)
                .limit(populationSize);
    }

    private Chromosome<T> createChromosome() {
        return producer.apply(Stream.generate(geneInitializer)
                .limit(chromosomeLength));
    }

    public int getChromosomeLength() {
        return chromosomeLength;
    }

    public Function<Stream<Gene<T>>, Chromosome<T>> getProducer() {
        return producer;
    }

    public GeneInitializer<T> getGeneInitializer() {
        return geneInitializer;
    }
}