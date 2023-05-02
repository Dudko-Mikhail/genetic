package by.dudko.genetic.model.gene;

import java.util.random.RandomGenerator;

public class IntegerGene extends Gene<Integer> { // todo refactor
    static IntegerGene of(int value) {
        return new IntegerGene(value);
    }

    static IntegerGene of(RandomGenerator randomGenerator, int min, int max) {
        return new IntegerGene(randomGenerator.nextInt(min, max));
    }


    static IntegerGene of(RandomGenerator randomGenerator, int min) {
        return new IntegerGene(randomGenerator.nextInt(min));
    }

    public IntegerGene(int value) {
        super(value);
    }
}
