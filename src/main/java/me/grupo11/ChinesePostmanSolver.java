package me.grupo11;

import java.util.*;

public class ChinesePostmanSolver implements ChinesePostman {

    private final Graph graph;

    public ChinesePostmanSolver(Graph graph) {
        this.graph = graph;
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

    public Iterator<Integer> iteratorEulerianTrailOrCycle() {

        boolean[][] adjacencyMatrix = new boolean[this.graph.getVerticesCount()][this.graph.getVerticesCount()];
        for (int i = 0; i < this.graph.getVerticesCount(); i++) {
            for (int j = 0; j < this.graph.getVerticesCount(); j++) {
                adjacencyMatrix[i][j] = false;
            }
        }

        for (int v = 0; v < this.graph.getVerticesCount(); v++) {
            int selfLoops = 0;

            for (int w : this.graph.getNeighbors(v)) {
                if (v == w) {
                    if (selfLoops % 2 == 0) {
                        adjacencyMatrix[v][w] = true;
                    }
                    selfLoops++;
                } else if (v < w) {
                    adjacencyMatrix[v][w] = true;
                }
            }
        }

        return this.iteratorDFS(adjacencyMatrix, this.getFirstNonIsolatedVertex());
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

    /**
     * Find paths that are dead-ends.
     * We know we have to double them, since they are all order 1.
     *
     * @return List<Integer>
     */
    private List<Integer> getSingleNodes() {
        List<Integer> singleNodes = new LinkedList<>();

        for (int i = 0; i < this.graph.getVerticesCount(); i++) {
            if (this.graph.getNeighbors(i).size() == 1) {
                singleNodes.add(i);
            }
        }

        return singleNodes;
    }

    private int getFirstNonIsolatedVertex() {
        for (int v = 0; v < this.graph.getVerticesCount(); v++) {
            if (this.graph.getNeighbors(v).size() > 1) {
                return v;
            }
        }

        return -1;
    }

    private Iterator<Integer> iteratorDFS(boolean[][] adjacencyMatrix, int startVertex) {
        Stack<Integer> traversalStack = new Stack<>();
        List<Integer> resultList = new LinkedList<>();

        boolean[] visited = new boolean[adjacencyMatrix.length];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            visited[i] = false;
        }

        traversalStack.push(startVertex);
        resultList.add(startVertex);
        visited[startVertex] = true;

        while (!traversalStack.isEmpty()) {
            int x = traversalStack.peek();
            boolean found = false;

            /* Find a vertex adjacent to x that has not been visited
             and push it on the stack */
            for (int i = 0; (i < adjacencyMatrix.length) && !found; i++) {
                if (adjacencyMatrix[x][i] && !visited[i]) {
                    traversalStack.push(i);
                    resultList.add(i);
                    visited[i] = true;
                    found = true;
                }
            }

            if (!found && !traversalStack.isEmpty()) {
                traversalStack.pop();
            }
        }

        return resultList.iterator();
    }


    private List<List<Edge<Integer>>> getMinimumPerfectMatching(List<Integer> oddVertices) {

        int n = oddVertices.size() / 2;

        List<List<Edge<Integer>>> matchingList = new ArrayList<>(new ArrayList<>());

        for (int i = 0; i < n; i++) {
            int index = 0;
            int removeIndex;
            Iterator<Integer> iterator = oddVertices.iterator();
            Integer v = iterator.next();

            matchingList.set(i, this.graph.getShortestPath(v, iterator.next()));
            removeIndex = ++index;

            List<Edge<Integer>> currentShortestPath;

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

    private void duplicateEdges(List<List<Edge<Integer>>> edges) {
        Iterator<Edge<Integer>> matchIterator;
        Edge<Integer> last;
        Edge<Integer> current;

        for (List<Edge<Integer>> match : edges) {
            matchIterator = match.iterator();
            last = matchIterator.next();

            while (matchIterator.hasNext()) {
                current = matchIterator.next();
                this.graph.addEdge(last.from, current.from, current.weight);
                last = current;
            }
        }
    }

}
