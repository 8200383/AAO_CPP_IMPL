package me.grupo11;

import java.util.*;

public class UndirectedGraph implements Graph {
    private final int[][] adjacencyMatrix;
    private int vertices;
    private int edges;

    public UndirectedGraph(int vertices) {
        this.adjacencyMatrix = new int[vertices][vertices];
        this.vertices = vertices;
        this.edges = 0;

        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j < this.adjacencyMatrix.length; j++) {
                this.adjacencyMatrix[i][j] = -1;
            }
        }
    }

    @Override
    public void addEdge(int x, int y, int weight) {
        this.adjacencyMatrix[x][y] = weight;
        this.adjacencyMatrix[y][x] = weight;

        this.edges += 2;
        this.vertices++;
    }

    private boolean indexIsValid(int index) {
        return index < this.adjacencyMatrix.length && index >= 0;
    }

    @Override
    public int getVerticesCount() {
        return this.vertices;
    }

    @Override
    public int getEdgesCount() {
        return this.edges;
    }

    @Override
    public boolean isConnected() {
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j < i; j++) {
                if (this.adjacencyMatrix[i][j] != this.adjacencyMatrix[j][i]) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public List<Integer> getNeighbors(int x) {
        List<Integer> neighbors = new ArrayList<>();

        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            if (this.adjacencyMatrix[x][i] != -1 && this.adjacencyMatrix[i][x] != -1) {
                neighbors.add(i);
            }
        }

        return neighbors;
    }

    public List<Edge<Integer, Integer>> getShortestPath(int x, int y) {
        List<Edge<Integer, Integer>> predecessor = new LinkedList<>();
        Queue<Integer> traversalQueue = new LinkedList<>();

        if (!indexIsValid(x) || !indexIsValid(y) || x == y) {
            return Collections.emptyList();
        }

        boolean[] visited = new boolean[this.adjacencyMatrix.length];
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            visited[i] = false;
        }

        traversalQueue.add(x);
        visited[x] = true;
        predecessor.add(new Edge<>(x, 0));

        int index = x;
        while (!traversalQueue.isEmpty() && (index != y)) {
            index = traversalQueue.remove();

            for (int i = 0; i < this.adjacencyMatrix.length; i++) {
                if (this.adjacencyMatrix[index][i] != -1 && !visited[i]) {
                    predecessor.add(new Edge<>(index, this.adjacencyMatrix[index][i]));
                    traversalQueue.add(i);
                    visited[i] = true;
                }
            }
        }

        // no path must have been found
        if (index != y) {
            return Collections.emptyList();
        }

        int lastWeight = this.adjacencyMatrix[predecessor.get(index).vertex][y];
        predecessor.add(new Edge<>(y, lastWeight));

        Collections.reverse(predecessor);

        return predecessor;
    }

    @Override
    public String plot(boolean download) {
        String url = "https://quickchart.io/graphviz?layout=neato&graph=graph{";
        StringBuilder graph = new StringBuilder(url);

        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (this.adjacencyMatrix[i][j] != -1) {
                    graph.append(j).append("--").append(i).append(';');
                }
            }
        }

        graph.append('}');

        if (download) graph.append("&format=png");

        return graph.toString();
    }

    @Override
    public String toString() {
        StringBuilder matrix = new StringBuilder();

        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            matrix.append("[").append(i).append("] ");
            for (int j = 0; j < this.adjacencyMatrix.length; j++) {
                matrix.append(this.adjacencyMatrix[i][j]).append(" | ");
            }
            matrix.append("\n");
        }


        return "UndirectedGraph{\n" + matrix + "}";
    }
}
