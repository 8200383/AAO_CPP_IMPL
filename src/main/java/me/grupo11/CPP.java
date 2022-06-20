package me.grupo11;

import java.util.*;

public class CPP implements GraphADT {
    private final int[][] adjacencyMatrix;
    private int vertices;
    private int edges;

    public CPP(int vertices) {
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

    @Override
    public List<Integer> getSingleNodes() {
        List<Integer> singleNodes = new LinkedList<>();

        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            if (this.getNeighbors(i).size() == 1) {
                singleNodes.add(i);
            }
        }

        return singleNodes;
    }

    @Override
    public boolean isEulerian() {
        if (!this.isConnected()) return false;

        // Count vertices with odd degree
        int odd = 0;
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            if (this.getNeighbors(i).size() % 2 != 0) {
                odd++;
            }
        }

        return odd < 2;
    }

    @Override
    public Iterator<Integer> iteratorEulerianTrailOrCycle() {
        return Collections.emptyIterator();
    }

    @Override
    public Iterator<Integer> iteratorPostmanCycle() {

        /*
         * Make Eulerian (Python)
         * https://github.com/supermitch/Chinese-Postman/blob/f07cecb4c937e2fd96eada37cee7a65aa9f32d79/chinesepostman/eularian.py
         */

        if (!isEulerian()) {
            // Add necessary paths to the graph such that it becomes Eulerian.

            List<Integer> singleNodes = this.getSingleNodes(); // Find dead-ends

            // Building possible odd node pairs
            // Finding pair solutions
            // Building path sets
            // Finding cheapest route with iteratorShortestPath
            // Adding new edges

            // Let it continue to iteratorEulerianTrailOrCycle
        }

        return this.iteratorEulerianTrailOrCycle();
    }

    public Iterator<Integer> iteratorShortestPath(int x, int y) {
        int index = x;
        int[] predecessor = new int[this.adjacencyMatrix.length];
        Queue<Integer> traversalQueue = new LinkedList<>();
        List<Integer> resultList = new LinkedList<>();

        if (!indexIsValid(x) || !indexIsValid(y) || x == y) {
            return Collections.emptyIterator();
        }

        boolean[] visited = new boolean[this.adjacencyMatrix.length];
        for (int i = 0; i < this.adjacencyMatrix.length; i++)
            visited[i] = false;

        traversalQueue.add(x);
        visited[x] = true;
        predecessor[x] = -1;

        while (!traversalQueue.isEmpty() && (index != y)) {
            index = traversalQueue.remove();

            for (int i = 0; i < this.adjacencyMatrix.length; i++) {
                if (this.adjacencyMatrix[index][i] != -1 && !visited[i]) {
                    predecessor[i] = index;
                    traversalQueue.add(i);
                    visited[i] = true;
                }
            }
        }

        // no path must have been found
        if (index != y) {
            return Collections.emptyIterator();
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(y);

        do {
            index = predecessor[index];
            stack.push(index);
        } while (index != x);

        while (!stack.isEmpty()) {
            resultList.add(stack.pop());
        }

        return resultList.iterator();
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
