package me.grupo11;

import java.util.Iterator;
import java.util.List;

public interface ChinesePostman {

    /**
     * Find paths that are dead-ends.
     * We know we have to double them, since they are all order 1.
     *
     * @return List<Integer>
     */
    List<Integer> getSingleNodes();

    /**
     * Returns true if the input graph is an Eulerian graph,
     * i.e there exists a closed walk in the graph that uses each edge exactly once.
     * It returns false otherwise.
     *
     * @return Iterator<Integer>
     */
    boolean isEulerian();

    /**
     * Return an Eularian Trail or Eularian Circuit through a graph, if found.
     *
     * @return Iterable<Integer>
     */
    Iterator<Integer> iteratorEulerianTrailOrCycle();

    /**
     * Returns an iterator that contains shortest path
     * or circuity that visits every edge of the graph at least once.
     *
     * @return Iterator<Integer>
     */
    Iterator<Integer> iteratorPostmanCycle();
}
