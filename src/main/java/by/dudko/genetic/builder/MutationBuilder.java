package by.dudko.genetic.builder;

import by.dudko.genetic.model.chromosome.Chromosome;
import by.dudko.genetic.model.gene.Gene;
import by.dudko.genetic.stepProcess.stepMutation.PopulationMutation;

import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class MutationBuilder<T> { // todo возможно данный класс, находиться внутри geneticAlgorithmBuilder
    private double chromosomeMutationProbability = 0.1;

    public MutationBuilder() {
    }

    public GeneBasedMutationBuilder geneBasedMutation() {
        return new GeneBasedMutationBuilder();
    }

    public ChromosomeBasedMutationBuilder chromosomeBasedMutationBuilder() {
        return new ChromosomeBasedMutationBuilder();
    }

    public MutationBuilder<T> chromosomeMutationProbability(double chromosomeMutationProbability) {
        this.chromosomeMutationProbability = chromosomeMutationProbability;
        return this;
    }

    public class ChromosomeBasedMutationBuilder {
        private RandomGenerator random;
        private UnaryOperator<Chromosome<T>> chromosomeMutator;

        public PopulationMutation<T> build() {
            return PopulationMutation.of(random, chromosomeMutationProbability, chromosomeMutator);
        }

        public ChromosomeBasedMutationBuilder chromosomeMutator(UnaryOperator<Chromosome<T>> chromosomeMutator) {
            this.chromosomeMutator = chromosomeMutator;
            return this;
        }

        public ChromosomeBasedMutationBuilder random(RandomGenerator random) {
            this.random = random;
            return this;
        }
    }

    public class GeneBasedMutationBuilder {
        private double geneMutationProbability = 0.1;
        private RandomGenerator random;
        private UnaryOperator<Gene<T>> geneMutator;

        public PopulationMutation<T> build() {
            return PopulationMutation.of(random, chromosomeMutationProbability, geneMutationProbability,
                    geneMutator);
        }

        public GeneBasedMutationBuilder geneMutator(UnaryOperator<Gene<T>> geneMutator) {
            this.geneMutator = geneMutator;
            return this;
        }

        public GeneBasedMutationBuilder random(RandomGenerator random) {
            this.random = random;
            return this;
        }

        public GeneBasedMutationBuilder geneMutationProbability(double geneMutationProbability) {
            this.geneMutationProbability = geneMutationProbability;
            return this;
        }
    }
}

class zeta { // todo remove
    public static void main(String[] args) {
        PopulationMutation<Integer> populationMutation = new MutationBuilder<Integer>()
                .chromosomeMutationProbability(0.5)
                .geneBasedMutation()
                .geneMutationProbability(0.7)
                .geneMutator(GeneMutationFactory.produceMutagen(GeneMutationFactory.GeneMutationType.EXCHANGE))
                .build();
    }
}

class GeneMutationFactory { /* TODO вместо geneMutationType будут использоваться отдельные методы,
                                    т.к. различные виды мутаций требуют различные параметры для конфигурации */
    private GeneMutationFactory() {
    }

    enum GeneMutationType {
        EXCHANGE, SHUFFLE
    }

    public static <T> UnaryOperator<Gene<T>> produceMutagen(GeneMutationType mutationType) {
        return switch (mutationType) {
            default -> null;
        };
    }
}

class ChromosomeMutationFactory { /* TODO вместо chromosomeMutationType будут использоваться отдельные методы */
    private ChromosomeMutationFactory() {
    }

    enum ChromosomeMutationType {
        FIRST, SECOND,
    }

    public static <T> UnaryOperator<Chromosome<T>> produceChromosomeMutator(ChromosomeMutationType mutationType) {
        return switch (mutationType) {
            default -> null;
        };
    }
}

