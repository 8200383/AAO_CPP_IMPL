package me.grupo11;

import java.util.*;

public class CPP implements GraphADT {
    private final int[][] adjacencyMatrix;
    private int edgeNum;

    private static class Edge {
        private final int v;
        private final int w;
        private boolean isUsed;

        public Edge(int v, int w) {
            this.v = v;
            this.w = w;
            isUsed = false;
        }

        // returns the other vertex of the edge
        public int other(int vertex) {
            if (vertex == v) return w;
            else if (vertex == w) return v;
            else throw new IllegalArgumentException("Illegal endpoint");
        }
    }

    public CPP(int vertices) {
        this.adjacencyMatrix = new int[vertices][vertices];
        this.edgeNum = 0;
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            for (int j = 0; j < this.adjacencyMatrix.length; j++) {
                this.adjacencyMatrix[i][j] = -1;
            }
        }
    }

    @Override
    public void addEdge(int x, int y, int weight) {
        this.adjacencyMatrix[x][y] = weight;
        this.adjacencyMatrix[y][x] = weight;
        edgeNum++;
    }

    private boolean indexIsValid(int index) {
        return index < this.adjacencyMatrix.length && index >= 0;
    }

    @Override
    public Iterator<Integer> iteratorEulerianTrailOrCycle() {
        Stack<Integer> cycle;
        // must have at least one edge
        // must have at least one edge
        if (this.edgeNum == 0) return Collections.emptyIterator();

        // necessary condition: all vertices have even degree
        // (this test is needed or it might find an Eulerian path instead of cycle)
        for (int v = 0; v < this.size(); v++)
            if (this.getNeighbors(v).size() % 2 != 0)
                return Collections.emptyIterator();

        // create local view of adjacency lists, to iterate one vertex at a time
        // the helper Edge data type is used to avoid exploring both copies of an edge v-w
        Queue<Edge>[] adj = (Queue<Edge>[]) new Queue[this.size()];
        for (int v = 0; v < this.size(); v++)
            adj[v] = new LinkedList<>();

        for (int v = 0; v < this.size(); v++) {
            int selfLoops = 0;
            for (int w : this.getNeighbors(v)) {
                // careful with self loops
                if (v == w) {
                    if (selfLoops % 2 == 0) {
                        Edge e = new Edge(v, w);
                        adj[v].add(e);
                        adj[w].add(e);
                    }
                    selfLoops++;
                } else if (v < w) {
                    Edge e = new Edge(v, w);
                    adj[v].add(e);
                    adj[w].add(e);
                }
            }
        }

        // initialize stack with any non-isolated vertex
        int s = nonIsolatedVertex();
        Stack<Integer> stack = new Stack<>();
        stack.push(s);

        // greedily search through edges in iterative DFS style
        cycle = new Stack<>();
        while (!stack.isEmpty()) {
            int v = stack.pop();
            while (!adj[v].isEmpty()) {
                Edge edge = adj[v].remove();
                if (edge.isUsed) continue;
                edge.isUsed = true;
                stack.push(v);
                v = edge.other(v);
            }
            // push vertex with no more leaving edges to cycle
            cycle.push(v);
        }

        // check if all edges are used
        if (cycle.size() != this.edgeNum + 1)
            return Collections.emptyIterator();

        return cycle.iterator();
    }

    @Override
    public int size() {
        return this.adjacencyMatrix.length;
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
            if (this.adjacencyMatrix[x][i] != -1) {
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
            List<Integer> neighbours = this.getNeighbors(i);

            if (neighbours.size() % 2 != 0) {
                odd++;
            }
        }

        return odd < 2;
    }

    private int nonIsolatedVertex() {
        for (int v = 0; v < this.size(); v++)
            if (this.getNeighbors(v).size() > 0)
                return v;
        return -1;
    }

    @Override
    public Iterator<Integer> iteratorPostmanCycle() {
        if (!isEulerian()) {
            // Add necessary paths to the graph such that it becomes Eulerian.

            List<Integer> singleNodes = this.getSingleNodes(); // Find dead-ends

            // Double our dead-ends
        }

        return this.iteratorEulerianTrailOrCycle();
    }

    public Iterator<Integer> iteratorShortestPath(int x, int y) {
        int index = x;
        int[] predecessor = new int[this.adjacencyMatrix.length];
        Queue<Integer> traversalQueue = new LinkedList<>();
        List<Integer> resultList = new LinkedList<>();

        if (!indexIsValid(x) || !indexIsValid(y) || x == y) {
            return Collections.emptyIterator();
        }

        boolean[] visited = new boolean[this.adjacencyMatrix.length];
        for (int i = 0; i < this.adjacencyMatrix.length; i++)
            visited[i] = false;

        traversalQueue.add(x);
        visited[x] = true;
        predecessor[x] = -1;

        while (!traversalQueue.isEmpty() && (index != y)) {
            index = traversalQueue.remove();

            for (int i = 0; i < this.adjacencyMatrix.length; i++) {
                if (this.adjacencyMatrix[index][i] != -1 && !visited[i]) {
                    predecessor[i] = index;
                    traversalQueue.add(i);
                    visited[i] = true;
                }
            }
        }

        // no path must have been found
        if (index != y) {
            return Collections.emptyIterator();
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(y);

        do {
            index = predecessor[index];
            stack.push(index);
        } while (index != x);

        while (!stack.isEmpty()) {
            resultList.add(stack.pop());
        }

        return resultList.iterator();
    }

    @Override
    public String plot(boolean download) {
        String url = "https://quickchart.io/graphviz?graph=graph{";
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
