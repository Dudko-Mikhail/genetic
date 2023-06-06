package by.dudko.genetic.model.gene;

public class DoubleGene extends NumericGene<Double, DoubleGene> {
    public DoubleGene(double value) {
        this(value, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleGene(double value, double min, double max) {
        super(value, min, max, Double::compareTo);
    }

    @Override
    public DoubleGene newInstance(Double value) {
        return new DoubleGene(value);
    }

    @Override
    public DoubleGene newInstance(Double value, Double min, Double max) {
        return new DoubleGene(value, min, max);
    }
}
