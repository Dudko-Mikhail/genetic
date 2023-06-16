package by.dudko.genetic.example.salesman;

import java.util.Map;

public class LittleAlgorithm { // todo implement
    private final int[][] graph;
    private final int n;
    private int currentPenalty = 0;

    public LittleAlgorithm(int[][] graph) {
        this.graph = graph;
        this.n = graph.length;
    }

    public int[] findRoute() {
        return null;
    }

    private int[] findRowsMinimums(int[][] matrix) {
        int[] minimums = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int min = matrix[i][0];
            for (int j = 1; j < matrix.length; j++) {
                if (matrix[i][j] < min) {
                    min = matrix[i][j];
                }
            }
            minimums[i] = min;
        }
        return minimums;
    }

    private int[] findColumnsMinimums(int[][] matrix) {
        int[] minimums = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int min = matrix[0][i];
            for (int j = 1; j < matrix.length; j++) {
                if (matrix[j][i] < min) {
                    min = matrix[j][i];
                }
            }
            minimums[i] = min;
        }
        return minimums;
    }

    private Map.Entry<Integer, Integer> findZeroWithMaxPenalty(int[][] matrix, int[] rowPenalties, int[] columnPenalties) {
        int maxPenalty = -1;
        int row = 0;
        int column = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == 0) {
                    int penalty = rowPenalties[i] + columnPenalties[j];
                    if (penalty > maxPenalty) {
                        row = i;
                        column = j;
                    }
                    break;
                }
            }
        }
        return Map.entry(row, column);
    }

    ;
}
