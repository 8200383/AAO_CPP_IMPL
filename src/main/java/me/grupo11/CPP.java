package me.grupo11;

import java.util.*;

public class CPP implements GraphADT {
    private final int[][] adjacencyMatrix;

    public CPP(int vertices) {
        this.adjacencyMatrix = new int[vertices][vertices];

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
    }

    @Override
    public int size() {
        return this.adjacencyMatrix.length;
    }

    @Override
    public boolean isConnected() {
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j < this.adjacencyMatrix.length; j++) {
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
            if (this.adjacencyMatrix[x][i] != -1) {
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
            List<Integer> neighbours = this.getNeighbors(i);

            if (neighbours.size() % 2 != 0) {
                odd++;
            }
        }

        return odd < 2;
    }

    @Override
    public Iterator<Integer> getEulerianTrailOrCycle() {
        return Collections.emptyIterator();
    }

    @Override
    public Iterator<Integer> getPostmanCycle() {
        if (!isEulerian()) {
            // Add necessary paths to the graph such that it becomes Eulerian.

            List<Integer> singleNodes = this.getSingleNodes(); // Find dead-ends

            // Double our dead-ends
        }

        return this.getEulerianTrailOrCycle();
    }

    @Override
    public String plot() {
        StringBuilder graphLink = new StringBuilder("https://quickchart.io/graphviz?graph=graph{");
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (this.adjacencyMatrix[i][j] != -1) {
                    graphLink.append(j).append("--").append(i).append(';');
                }
            }
        }

        graphLink.append('}');
        return graphLink.toString();
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