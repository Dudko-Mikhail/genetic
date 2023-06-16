package by.dudko.genetic.process.mutation.impl;

import by.dudko.genetic.model.gene.Gene;

import java.util.function.UnaryOperator;

public class FlipBit<G extends Gene<Boolean, G>> implements UnaryOperator<G> {
    @Override
    public G apply(G gene) {
        return gene.newInstance(!gene.getValue());
    }
}
