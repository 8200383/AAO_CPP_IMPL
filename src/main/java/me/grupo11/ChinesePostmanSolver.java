package me.grupo11;

import java.util.*;

public class ChinesePostmanSolver implements ChinesePostman {

    private final Graph graph;

    public ChinesePostmanSolver(Graph graph) {
        this.graph = graph;
    }

    private List<List<Edge<Integer, Integer>>> getMinimumPerfectMatching(List<Integer> oddVertices) {

        int n = oddVertices.size() / 2;

        List<List<Edge<Integer, Integer>>> matchingList = new ArrayList<>(new ArrayList<>());

        for (int i = 0; i < n; i++) {
            int index = 0;
            int removeIndex;
            Iterator<Integer> iterator = oddVertices.iterator();
            Integer v = iterator.next();

            matchingList.set(i, this.graph.getShortestPath(v, iterator.next()));
            removeIndex = ++index;

            List<Edge<Integer, Integer>> currentShortestPath;

            while (iterator.hasNext()) {
                Integer next = iterator.next();
                currentShortestPath = this.graph.getShortestPath(v, next);
                index++;

                if (currentShortestPath.size() < matchingList.get(i).size()) {
                    matchingList.set(i, currentShortestPath);
                    removeIndex = index;
                }
            }

            oddVertices.remove(removeIndex);
            oddVertices.remove(0);
        }

        return matchingList;
    }

    private void duplicateEdges(List<List<Edge<Integer, Integer>>> edges) {
        Iterator<Edge<Integer, Integer>> matchIterator;
        Edge<Integer, Integer> last;
        Edge<Integer, Integer> current;

        for (List<Edge<Integer, Integer>> match : edges) {
            matchIterator = match.iterator();
            last = matchIterator.next();

            while (matchIterator.hasNext()) {
                current = matchIterator.next();
                this.graph.addEdge(last.vertex, current.vertex, current.weight);
                last = current;
            }
        }
    }

    @Override
    public List<Integer> getSingleNodes() {
        List<Integer> singleNodes = new LinkedList<>();

        for (int i = 0; i < this.graph.getVerticesCount(); i++) {
            if (this.graph.getNeighbors(i).size() == 1) {
                singleNodes.add(i);
            }
        }

        return singleNodes;
    }

    @Override
    public boolean isEulerian() {
        if (!this.graph.isConnected()) return false;

        // Count vertices with odd degree
        int odd = 0;
        for (int i = 0; i < this.graph.getVerticesCount(); i++) {
            if (this.graph.getNeighbors(i).size() % 2 != 0) {
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
            // Find dead-ends
            List<Integer> singleNodes = this.getSingleNodes();

            // Add necessary paths to the graph such that it becomes Eulerian
            this.duplicateEdges(this.getMinimumPerfectMatching(singleNodes));
        }

        return this.iteratorEulerianTrailOrCycle();
    }

}
