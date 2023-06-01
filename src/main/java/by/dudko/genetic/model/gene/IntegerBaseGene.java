package by.dudko.genetic.model.gene;

import java.util.random.RandomGenerator;

public class IntegerBaseGene extends BaseGene<Integer> { // todo refactor
    public static IntegerBaseGene of(int value) {
        return new IntegerBaseGene(value);
    }

    public static IntegerBaseGene of(RandomGenerator randomGenerator, int min, int max) {
        return new IntegerBaseGene(randomGenerator.nextInt(min, max));
    }


    public static IntegerBaseGene of(RandomGenerator randomGenerator, int max) {
        return new IntegerBaseGene(randomGenerator.nextInt(max));
    }

    public IntegerBaseGene(int value) {
        super(value);
    }
}
