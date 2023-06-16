package by.dudko.genetic.example.salesman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LocalSearch {
    private int[][] graph;

    public LocalSearch(int[][] graph) {
        this.graph = graph;
    }

    public void doAlgorithm() {
        int[] route;
        GreedyAlgorithm algorithms = new GreedyAlgorithm(graph);
        route = algorithms.buildRoute();
        System.out.println("initial route " + Arrays.toString(route));
        System.out.println("Initial length " + calculateRouteLength(route));

        boolean needsOptimization = true;
        while (needsOptimization) {
            needsOptimization = wasSomethingImproved(route);
        }
        System.out.println("After optimization " + Arrays.toString(route));
        System.out.println("final length " + calculateRouteLength(route));
    }

    public int[] optimize(int[] route) {
        int[] copy = Arrays.copyOf(route, route.length);
//        System.out.println("Before optimization: " + calculateRouteLength(route));
        boolean needsOptimization = true;
        while (needsOptimization) {
            needsOptimization = wasSomethingImproved(copy);
        }
//        System.out.println("After optimization: " + calculateRouteLength(copy));
        return copy;
    }

    public int[] generateRandomRoute() {
        Random random = new Random();
        int[] route = new int[graph.length];
        List<Integer> unusedVertices = generateIndexSequence(graph.length);
        int i = 0;
        while (!unusedVertices.isEmpty()) {
            int randomIndex = random.nextInt(unusedVertices.size());
            route[i++] = unusedVertices.get(randomIndex);
            unusedVertices.remove(randomIndex);
        }
        return route;
    }

    private List<Integer> generateIndexSequence(int length) {
        List<Integer> sequence = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            sequence.add(i);
        }
        return sequence;
    }

    private int calculateRouteLength(int[] route) {
        int endIndex = route.length - 1;
        int routeLength = graph[route[0]][route[endIndex]];
        for (int i = 0; i < endIndex; i++) {
            routeLength += graph[route[i]][route[i + 1]];
        }
        return routeLength;
    }

    private boolean wasSomethingImproved(int[] route) {
        boolean wasSomethingImproved = false;
        int n = route.length;
        int initialLength = calculateRouteLength(route);
        for (int i = 0; i < n; i++) {
            int nextVertexIndex = (i + 2) % n;
            swap(route, i, nextVertexIndex);
            int updatedLength = calculateRouteLength(route);
            if (updatedLength < initialLength) {
//                System.out.println("swap operation (i,j)=(" + i + "," + nextVertexIndex + ")");
                initialLength = updatedLength;
                wasSomethingImproved = true;
            } else {
                swap(route, i, nextVertexIndex);
            }
        }
        return wasSomethingImproved;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
