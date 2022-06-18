package me.grupo11;

import java.util.Iterator;
import java.util.List;

public interface GraphADT {
    void addEdge(int x, int y, int weight);

    int size();

    boolean isConnected();

    /**
     * Find paths that are dead-ends.
     * We know we have to double them, since they are all order 1.
     *
     * @return List<Integer> Return a list of dead-ended edges.
     */
    List<Integer> getSingleNodes();

    List<Integer> getNeighbors(int x);

    boolean isEulerian();

    /**
     * Return an Eularian Trail or Eularian Circuit through a graph, if found.
     *
     * @return Iterable<Integer>
     */
    Iterator<Integer> getEulerianTrailOrCycle();

    Iterator<Integer> getPostmanCycle();

    Iterator<Integer> iteratorShortestPath(int x, int y);

    String plot(boolean download);
}
