package by.dudko.genetic.builder.mutation;

import by.dudko.genetic.model.gene.BaseGene;

import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

public class GeneMutationFactory {
    private GeneMutationFactory() {
    }

    public static UnaryOperator<BaseGene<Boolean>> flipBit() {
        return (gene -> gene.newInstance(!gene.getValue()));
    }
}
