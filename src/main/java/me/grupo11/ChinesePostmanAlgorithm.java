package me.grupo11;

import java.util.List;

public class ChinesePostmanAlgorithm {

    private final Graph graph;

    ChinesePostmanAlgorithm(Graph graph) {
        this.graph = graph;
    }

    private boolean isEulerian() {
        if (!this.graph.isConnected()) return false;

        // Count vertices with odd degree
        int odd = 0;
        for (int i = 0; i < this.graph.size(); i++) {
            List<Integer> neighbours = this.graph.getNeighbors(i);

            if (neighbours.size() % 2 != 0) {
                odd++;
            }
        }

        return odd < 2;
    }
}
