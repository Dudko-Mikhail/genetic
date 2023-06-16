package by.dudko.genetic.builder.mutation;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BooleanGene;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.mutation.PopulationMutation;
import by.dudko.genetic.util.IndexSelector;
import by.dudko.genetic.util.IndexSelectors;

import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class MutationBuilder<G extends Gene<?, G>, F> { // todo переместить и доделать или удалить
    private double chromosomeMutationProbability = 0.1;

    public MutationBuilder() {
    }

    public GeneBasedMutationBuilder geneBasedMutation() {
        return new GeneBasedMutationBuilder();
    }

    public ChromosomeBasedMutationBuilder chromosomeBasedMutationBuilder() {
        return new ChromosomeBasedMutationBuilder();
    }

    public MutationBuilder<G, F> chromosomeMutationProbability(double chromosomeMutationProbability) {
        this.chromosomeMutationProbability = chromosomeMutationProbability;
        return this;
    }

    public class ChromosomeBasedMutationBuilder {
        private RandomGenerator random;
        private UnaryOperator<Chromosome<G>> chromosomeMutator;

        public PopulationMutation<G, F> build() {
            return PopulationMutation.fromChromosomeMutator(IndexSelectors.probabilitySelector(random, chromosomeMutationProbability), chromosomeMutator);
        }

        public ChromosomeBasedMutationBuilder chromosomeMutator(UnaryOperator<Chromosome<G>> chromosomeMutator) {
            this.chromosomeMutator = chromosomeMutator;
            return this;
        }

        public ChromosomeBasedMutationBuilder random(RandomGenerator random) {
            this.random = random;
            return this;
        }
    }

    public class GeneBasedMutationBuilder {
        private IndexSelector<G> indexSelector;
        private UnaryOperator<G> geneMutator;

        public PopulationMutation<G, F> build() { // todo implement
            return null;
        }

        public GeneBasedMutationBuilder geneMutator(UnaryOperator<G> geneMutator) {
            this.geneMutator = geneMutator;
            return this;
        }

        public GeneBasedMutationBuilder indexSelector(IndexSelector<G> indexSelector) {
            this.indexSelector = indexSelector;
            return this;
        }
    }
}