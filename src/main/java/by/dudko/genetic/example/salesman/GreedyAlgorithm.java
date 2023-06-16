package by.dudko.genetic.example.salesman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GreedyAlgorithm {
    private final int[][] graph;

    public GreedyAlgorithm(int[][] graph) {
        int[][] copy = new int[graph.length][];
        for (int i = 0; i < graph.length; i++) {
            copy[i] = Arrays.copyOf(graph[i], graph[i].length);
        }
        this.graph = copy;
    }

    public int[] buildRoute() {
        int[] route = new int[graph.length];
        List<Integer> freeVertices = generateIndexSequence(route.length);
        freeVertices.remove(0);
        route[0] = 0;
        int lastUsedVertex = 0;
        int i = 1;
        while (freeVertices.size() > 1) {
            int nearestVertex = findFreeNearestVertex(lastUsedVertex, freeVertices);
            freeVertices.remove(Integer.valueOf(nearestVertex));
            route[i++] = nearestVertex;
            lastUsedVertex = nearestVertex;
        }
        route[route.length - 1] = freeVertices.get(0);
        return route;
    }

    private int findFreeNearestVertex(int vertex, List<Integer> freeVertices) {
        int firstVertex = freeVertices.get(0);
        int minDistance = graph[vertex][firstVertex];
        int nearestVertex = firstVertex;
        for (int i = 1; i < freeVertices.size(); i++) {
            int distance = graph[vertex][freeVertices.get(i)];
            if (graph[vertex][i] < minDistance) {
                minDistance = distance;
                nearestVertex = freeVertices.get(i);
            }
        }
        return nearestVertex;
    }

    private List<Integer> generateIndexSequence(int length) {
        List<Integer> sequence = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            sequence.add(i);
        }
        return sequence;
    }
}
