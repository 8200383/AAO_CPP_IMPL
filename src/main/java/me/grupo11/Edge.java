package me.grupo11;

public class Edge<V extends Integer, W extends Integer> implements Comparable<V> {
    public V vertex;
    public W weight;

    public Edge(V vertex, W weight) {
        this.vertex = vertex;
        this.weight = weight;
    }

    @Override
    public int compareTo(V comparable) {
        return comparable.compareTo(vertex);
    }
}