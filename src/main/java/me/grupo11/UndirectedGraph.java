package me.grupo11;

import java.util.ArrayList;
import java.util.List;

public class UndirectedGraph implements Graph {
    private final int[][] adjacencyMatrix;

    public UndirectedGraph(int vertices) {
        this.adjacencyMatrix = new int[vertices][vertices];

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                adjacencyMatrix[i][j] = -1;
            }
        }
    }

    @Override
    public void addEdge(int x, int y, int weight) {
        adjacencyMatrix[x][y] = weight;
        adjacencyMatrix[y][x] = weight;
    }

    @Override
    public int size() {
        return this.adjacencyMatrix.length;
    }

    @Override
    public boolean isConnected() {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] != adjacencyMatrix[j][i]) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public List<Integer> getNeighbors(int x) {
        ArrayList<Integer> neighbors = new ArrayList<>();

        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            if (this.adjacencyMatrix[x][i] != -1) {
                neighbors.add(i);
            }
        }

        return neighbors;
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
