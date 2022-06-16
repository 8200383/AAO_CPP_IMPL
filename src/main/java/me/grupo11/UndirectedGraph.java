package me.grupo11;

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
    public void addEdge(int posA, int posB, int weight) {
        adjacencyMatrix[posA][posB] = weight;
        adjacencyMatrix[posB][posB] = weight;
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
