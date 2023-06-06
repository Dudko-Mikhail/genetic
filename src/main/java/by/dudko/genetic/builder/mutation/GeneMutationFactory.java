package by.dudko.genetic.builder.mutation;

import by.dudko.genetic.model.gene.Gene;

import java.util.function.UnaryOperator;

public class GeneMutationFactory {
    private GeneMutationFactory() {
    }

    public static <G extends Gene<Boolean, G>> UnaryOperator<G> flipBit() {
        return (gene -> gene.newInstance(!gene.getValue()));
    }
}
