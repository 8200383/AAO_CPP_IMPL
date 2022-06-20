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

    private int nonIsolatedVertex() {
        for (int v = 0; v < this.size(); v++)
            if (this.graph.getNeighbors(v).size() > 0)
                return v;
        return -1;
    }

    private int size() {
        return this.graph.getAdjacencyMatrix().length;
    }

    public Iterator<Integer> iteratorEulerianTrailOrCycle() {
        Stack<Integer> cycle;
        // must have at least one edge
        // must have at least one edge
        if (this.graph.getEdgesCount() == 0) return Collections.emptyIterator();

        // necessary condition: all vertices have even degree
        // (this test is needed or it might find an Eulerian path instead of cycle)
        for (int v = 0; v < this.size(); v++)
            if (this.graph.getNeighbors(v).size() % 2 != 0)
                return Collections.emptyIterator();

        // create local view of adjacency lists, to iterate one vertex at a time
        // the helper Edge data type is used to avoid exploring both copies of an edge v-w
        Queue<Edge>[] adj = (Queue<Edge>[]) new Queue[this.size()];
        for (int v = 0; v < this.size(); v++)
            adj[v] = new LinkedList<>();

        for (int v = 0; v < this.size(); v++) {
            int selfLoops = 0;
            for (int w : this.graph.getNeighbors(v)) {
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
                // v = edge.other(v); - Awaiting rewriting!
            }
            // push vertex with no more leaving edges to cycle
            cycle.push(v);
        }

        // check if all edges are used
        if (cycle.size() != this.graph.getEdgesCount() + 1)
            return Collections.emptyIterator();

        return cycle.iterator();
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
