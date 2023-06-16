package by.dudko.genetic.model.gene;

public class BooleanGene implements Gene<Boolean, BooleanGene> { // todo enum
    public static final BooleanGene TRUE = new BooleanGene(true);
    public static final BooleanGene FALSE = new BooleanGene(false);
    public static final BooleanGene ONE = new BooleanGene(true);
    public static final BooleanGene ZERO = new BooleanGene(false);

    private final boolean value;

    public static BooleanGene invertedGene(boolean gene) {
        return gene ? FALSE : TRUE;
    }

    public BooleanGene(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public BooleanGene newInstance(Boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public String toString() { // todo refactor
        return Boolean.toString(value);
    }
}
