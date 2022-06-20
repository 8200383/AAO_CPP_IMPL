package me.grupo11;

import java.util.*;

public class CPP implements GraphADT {
    private final int[][] adjacencyMatrix;
    private int vertices;
    private int edges;

    public CPP(int vertices) {
        this.adjacencyMatrix = new int[vertices][vertices];
        this.vertices = vertices;
        this.edges = 0;

        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j < this.adjacencyMatrix.length; j++) {
                this.adjacencyMatrix[i][j] = -1;
            }
        }
    }

    private List<List<Edge<Integer, Integer>>> getMinimumPerfectMatching(List<Integer> oddVertices) {

        int n = oddVertices.size() / 2;

        List<List<Edge<Integer, Integer>>> matchingList = new ArrayList<>(new ArrayList<>());

        for (int i = 0; i < n; i++) {
            int index = 0;
            int removeIndex;
            Iterator<Integer> iterator = oddVertices.iterator();
            Integer v = iterator.next();

            matchingList.set(i, this.getShortestPath(v, iterator.next()));
            removeIndex = ++index;

            List<Edge<Integer, Integer>> currentShortestPath;

            while (iterator.hasNext()) {
                Integer next = iterator.next();
                currentShortestPath = this.getShortestPath(v, next);
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

    @Override
    public void addEdge(int x, int y, int weight) {
        this.adjacencyMatrix[x][y] = weight;
        this.adjacencyMatrix[y][x] = weight;

        this.edges += 2;
        this.vertices++;
    }

    private boolean indexIsValid(int index) {
        return index < this.adjacencyMatrix.length && index >= 0;
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
                this.addEdge(last.vertex, current.vertex, current.weight);
                last = current;
            }
        }
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

    @Override
    public int getVerticesCount() {
        return this.vertices;
    }

    @Override
    public int getEdgesCount() {
        return this.edges;
    }

    @Override
    public boolean isConnected() {
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j < i; j++) {
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
            if (this.adjacencyMatrix[x][i] != -1 && this.adjacencyMatrix[i][x] != -1) {
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
            if (this.getNeighbors(i).size() % 2 != 0) {
                odd++;
            }
        }

        return odd < 2;
    }

    @Override
    public Iterator<Integer> iteratorEulerianTrailOrCycle() {
        return Collections.emptyIterator();
    }

    public List<Edge<Integer, Integer>> getShortestPath(int x, int y) {
        List<Edge<Integer, Integer>> predecessor = new LinkedList<>();
        Queue<Integer> traversalQueue = new LinkedList<>();

        if (!indexIsValid(x) || !indexIsValid(y) || x == y) {
            return Collections.emptyList();
        }

        boolean[] visited = new boolean[this.adjacencyMatrix.length];
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            visited[i] = false;
        }

        traversalQueue.add(x);
        visited[x] = true;
        predecessor.add(new Edge<>(x, 0));

        int index = x;
        while (!traversalQueue.isEmpty() && (index != y)) {
            index = traversalQueue.remove();

            for (int i = 0; i < this.adjacencyMatrix.length; i++) {
                if (this.adjacencyMatrix[index][i] != -1 && !visited[i]) {
                    predecessor.add(new Edge<>(index, this.adjacencyMatrix[index][i]));
                    traversalQueue.add(i);
                    visited[i] = true;
                }
            }
        }

        // no path must have been found
        if (index != y) {
            return Collections.emptyList();
        }

        int lastWeight = this.adjacencyMatrix[predecessor.get(index).vertex][y];
        predecessor.add(new Edge<>(y, lastWeight));

        Collections.reverse(predecessor);

        return predecessor;
    }

    record Edge<V extends Integer, W extends Integer>(V vertex, W weight) implements Comparable<V> {

        @Override
        public int compareTo(V comparable) {
            return comparable.compareTo(vertex);
        }
    }

    @Override
    public String plot(boolean download) {
        String url = "https://quickchart.io/graphviz?layout=neato&graph=graph{";
        StringBuilder graph = new StringBuilder(url);

        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (this.adjacencyMatrix[i][j] != -1) {
                    graph.append(j).append("--").append(i).append(';');
                }
            }
        }

        graph.append('}');

        if (download) graph.append("&format=png");

        return graph.toString();
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
