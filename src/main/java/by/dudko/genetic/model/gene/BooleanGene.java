package by.dudko.genetic.model.gene;

public class BooleanGene implements Gene<Boolean, BooleanGene> { // todo enum
    private final boolean value;

    public BooleanGene(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public BooleanGene newInstance(Boolean value) {
        return new BooleanGene(value);
    }

    @Override
    public String toString() { // todo refactor
        return Boolean.toString(value);
    }
}
