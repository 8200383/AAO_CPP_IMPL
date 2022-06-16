package me.grupo11;

public interface Graph {
    void addEdge(int posA, int posB, int weight);

    boolean isConnected();
}
