package me.grupo11;

import java.util.List;

public interface Graph {
    void addEdge(int posA, int posB, int weight);

    int size();

    boolean isConnected();

    List<Integer> getNeighbors(int x);
}
