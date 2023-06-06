package by.dudko.genetic.model.gene;

public class IntegerGene extends NumericGene<Integer, IntegerGene> {
    public IntegerGene(int value) {
        this(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerGene(int value, int min, int max) {
        super(value, min, max, Integer::compareTo);
    }

    @Override
    public IntegerGene newInstance(Integer value) {
        return new IntegerGene(value);
    }

    @Override
    public IntegerGene newInstance(Integer value, Integer min, Integer max) {
        return new IntegerGene(value, min, max);
    }
}
