package me.grupo11;

import java.util.List;

public interface Graph {
    /**
     * Inserts an edge between two vertices of this graph.
     */
    void addEdge(int x, int y, int weight);

    /**
     * Returns the number of vertices in this graph.
     */
    int getVerticesCount();

    /**
     * Returns the number of edges in this graph.
     */
    int getEdgesCount();

    /**
     * Returns true if this graph is connected, false otherwise.
     *
     * @return boolean
     */
    boolean isConnected();

    /**
     * Returns a list of neighbors of vertices
     *
     * @return List<Integer>
     */
    List<Integer> getNeighbors(int x);

    /**
     * Returns the shortest path between the two vertices.
     *
     * @return Iterator<Integer>
     */
    List<Edge<Integer, Integer>> getShortestPath(int x, int y);

    String plot(boolean download);
}
