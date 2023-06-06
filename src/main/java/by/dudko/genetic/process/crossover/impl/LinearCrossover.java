package by.dudko.genetic.process.crossover.impl;

import by.dudko.genetic.model.gene.Gene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;

//public class LinearCrossover<T extends Number> implements ChromosomeCrossover<T> { // todo implement (numericChromosome/gene); Use Factory gene/chromosome or creators.
//    private final RandomGenerator random;
//    private final double minA;
//    private final double maxA;
//    private final double minB;
//    private final double maxB;
//    private Function<Double, T> mapper;
//
//    public LinearCrossover(RandomGenerator random, double min, double max) {
//        this(random, min, max, min, max);
//    }
//
//    public LinearCrossover(RandomGenerator random, double minA, double maxA,
//                           double minB, double maxB) {
//        this.random = Objects.requireNonNull(random);
//        this.minA = minA;
//        this.maxA = RequireUtils.less(minA, maxA);
//        this.minB = minB;
//        this.maxB = RequireUtils.less(minB, maxB);
//    }
//
//    @Override
//    public Chromosome<T> apply(Chromosome<T> first, Chromosome<T> second) { // todo использование неинициализированного mapper. Избавиться от mapper
//        double a = random.nextDouble(minA, maxA);
//        double b = random.nextDouble(minB, maxB);
//        int length = Math.min(first.length(), second.length());
//        int geneIndex = random.nextInt(length);
//        BaseGene<T> firstGene = first.getGene(geneIndex);
//        BaseGene<T> secondGene = second.getGene(geneIndex);
//        BaseGene<T> newGene = (BaseGene<T>) first.newGene(mapper.apply(a * firstGene.getValue().doubleValue()
//                + b * secondGene.getValue().doubleValue()));
//        List<BaseGene<T>> genes = new ArrayList<>();
//        for (int i = 0; i < length; i++) {
//            if (i != geneIndex) {
//                genes.add(first.getGene(i));
//            } else {
//                genes.add(newGene);
//            }
//        }
//        return first.newInstance(genes);
//    }
//}

class SpecialChromosome<G extends Gene<?, G>> {
    List<G> genes;

    public SpecialChromosome(Collection<? extends G> genes) {
        this.genes = new ArrayList<>(genes);
    }

    public G getGene(int index) {
        return genes.get(index);
    }

    public void setGenes(List<G> genes) {
        this.genes = genes;
    }

    public List<G> getGenes() {
        return genes;
    }

    public int length() {
        return genes.size();
    }

    SpecialChromosome<G> newInstance(Collection<? extends G> genes) {
        return new SpecialChromosome<>(genes);
    }
}

abstract class NumericGene<T extends Number,
        G extends NumericGene<T, G>> implements Gene<T, G> {
    public NumericGene(T value) {

    }

    @Override
    public T getValue() {
        return null;
    }

    abstract G fromNumber(Number value);
}

class IntegerGene extends NumericGene<Integer, IntegerGene> {
    public IntegerGene(int value) {
        super(value);
    }

    @Override
    public IntegerGene newInstance(Integer value) {
        return new IntegerGene(value);
    }

    @Override
    IntegerGene fromNumber(Number value) {
        return new IntegerGene(value.intValue());
    }
}

class SpecialLinearCrossover<G extends NumericGene<?, G>> implements BinaryOperator<SpecialChromosome<G>> {
    @Override
    public SpecialChromosome<G> apply(SpecialChromosome<G> first, SpecialChromosome<G> second) {
        Random random = new Random();
        double minA = Double.MIN_VALUE;
        double minB = minA;
        double maxA = Double.MAX_VALUE;
        double maxB = maxA;
        double a = random.nextDouble(minA, maxA);
        double b = random.nextDouble(minB, maxB);
        int length = Math.min(first.length(), second.length());
        int geneIndex = random.nextInt(length);
        G firstGene = first.getGene(geneIndex);
        G secondGene = second.getGene(geneIndex);
        G result = firstGene.fromNumber(firstGene.getValue().doubleValue() * a + secondGene.getValue().doubleValue() * b);
        List<G> genes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (i != geneIndex) {
                genes.add(first.getGene(i));
            } else {
                genes.add(result);
            }
        }
        return first.newInstance(genes);
    }
}


//class UniversalLinearCrossover<T> implements ChromosomeCrossover<T> { // todo implement (numericChromosome/gene); Use Factory gene/chromosome or creators.
//    private final T minA;
//    private final T maxA;
//    private final T minB;
//    private final T maxB;
//    private final ArithmeticOperations<T> operations;
//
//    public UniversalLinearCrossover(ArithmeticOperations<T> operations, T min, T max) {
//        this(operations, min, max, min, max);
//    }
//
//    public UniversalLinearCrossover(ArithmeticOperations<T> operations, T minA, T maxA,
//                                    T minB, T maxB) {
//        this.operations = Objects.requireNonNull(operations);
//        this.minA = minA;
//        this.maxA = maxA;
//        this.minB = minB;
//        this.maxB = maxB;
//    }
//
//    @Override
//    public Chromosome<T> apply(Chromosome<T> first, Chromosome<T> second) {
//        T a = operations.newInstance(minA, maxA);
//        T b = operations.newInstance(minB, maxB);
//        int length = Math.min(first.length(), second.length());
//        Random random = new Random();
//        int geneIndex = random.nextInt(length);
//        BaseGene<T> firstGene = first.getGene(geneIndex);
//        BaseGene<T> secondGene = second.getGene(geneIndex);
//        T value = operations.sum(operations.multiply(firstGene.getValue(), a), operations.multiply(secondGene.getValue(), b));
//        BaseGene<T> newGene = firstGene.newInstance(value);
//        List<BaseGene<T>> genes = new ArrayList<>();
//        for (int i = 0; i < length; i++) {
//            if (i != geneIndex) {
//                genes.add(first.getGene(i));
//            } else {
//                genes.add(newGene);
//            }
//        }
//        return first.newInstance(genes);
//    }
//}