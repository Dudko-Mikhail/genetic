package by.dudko.genetic.builder.mutation;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.BooleanGene;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.process.mutation.ChromosomeBasedMutation;
import by.dudko.genetic.process.mutation.PopulationMutation;
import by.dudko.genetic.util.IndexSelector;

import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class MutationBuilder<G extends Gene<?, G>> { // todo возможно данный класс, находиться внутри geneticAlgorithmBuilder
    private double chromosomeMutationProbability = 0.1;

    public MutationBuilder() {
    }

    public GeneBasedMutationBuilder geneBasedMutation() {
        return new GeneBasedMutationBuilder();
    }

    public ChromosomeBasedMutationBuilder chromosomeBasedMutationBuilder() {
        return new ChromosomeBasedMutationBuilder();
    }

    public MutationBuilder<G> chromosomeMutationProbability(double chromosomeMutationProbability) {
        this.chromosomeMutationProbability = chromosomeMutationProbability;
        return this;
    }

    public class ChromosomeBasedMutationBuilder {
        private RandomGenerator random;
        private UnaryOperator<Chromosome<G>> chromosomeMutator;

        public PopulationMutation<G> build() {
            return new ChromosomeBasedMutation<>(random, chromosomeMutationProbability, chromosomeMutator);
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
        private IndexSelector indexSelector;
        private UnaryOperator<G> geneMutator;

        public PopulationMutation<G> build() { // todo implement
            return null;
        }

        public GeneBasedMutationBuilder geneMutator(UnaryOperator<G> geneMutator) {
            this.geneMutator = geneMutator;
            return this;
        }

        public GeneBasedMutationBuilder indexSelector(IndexSelector indexSelector) {
            this.indexSelector = indexSelector;
            return this;
        }
    }
}

class zeta { // todo remove
    public static void main(String[] args) {
        PopulationMutation<BooleanGene> populationMutation = new MutationBuilder<BooleanGene>()
                .chromosomeMutationProbability(0.5)
                .geneBasedMutation()
//                .geneMutationProbability(0.7)
                .geneMutator(GeneMutationFactory.flipBit())
                .build();
    }
}

