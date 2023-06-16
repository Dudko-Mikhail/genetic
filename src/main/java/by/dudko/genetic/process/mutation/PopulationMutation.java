package by.dudko.genetic.process.mutation;

import by.dudko.genetic.model.Individual;
import by.dudko.genetic.model.Population;
import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.util.IndexSelector;
import by.dudko.genetic.util.IndexSelectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public interface PopulationMutation<G extends Gene<?, G>, F> extends UnaryOperator<Population<G, F>> {
    static <G extends Gene<?, G>, F> PopulationMutation<G, F> fromChromosomeMutator(
            IndexSelector<Chromosome<G>> chromosomeSelector, UnaryOperator<Chromosome<G>> chromosomeMutator) {
        return population -> {
            chromosomeSelector.selectIndexes(population.getIndividuals())
                    .forEach(index -> {
                        Individual<G, F> chromosome = population.getIndividual(index);
                        population.replaceIndividual(index, chromosomeMutator.apply(chromosome));
                    });
            return population;
        };
    }

    static <G extends Gene<?, G>, F> PopulationMutation<G, F> fromGeneMutator(IndexSelector<G> geneSelector,
                                                                              UnaryOperator<G> geneMutator) {
        return fromGeneMutator(IndexSelectors.allSelector(0), geneSelector, geneMutator);
    }

    static <G extends Gene<?, G>, F> PopulationMutation<G, F> fromGeneMutator(
            IndexSelector<Chromosome<G>> chromosomeSelector, IndexSelector<G> geneSelector,
            UnaryOperator<G> geneMutator) {
        return fromChromosomeMutator(chromosomeSelector, chromosome -> {
            List<G> genes = new ArrayList<>(chromosome.getGenes());
            geneSelector.selectIndexes(genes)
                    .forEach(index -> {
                        G gene = chromosome.getGene(index);
                        genes.set(index, geneMutator.apply(gene));
                    });
            return chromosome.newInstance(genes);
        });
    }

    default PopulationMutation<G, F> andThen(UnaryOperator<Population<G, F>> mutation) {
        Objects.requireNonNull(mutation);
        return population -> mutation.apply(this.apply(population));
    }

    default PopulationMutation<G, F> after(UnaryOperator<Population<G, F>> mutation) {
        Objects.requireNonNull(mutation);
        return population -> this.apply(mutation.apply(population));
    }
}
