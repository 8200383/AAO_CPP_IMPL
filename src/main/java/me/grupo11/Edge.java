package me.grupo11;

public class Edge<V extends Integer, W extends Integer> implements Comparable<V> {
    public V vertex;
    public W weight;
    public boolean isUsed;

    public Edge(V vertex, W weight) {
        this.vertex = vertex;
        this.weight = weight;
        this.isUsed = false;
    }

    @Override
    public int compareTo(V comparable) {
        return comparable.compareTo(vertex);
    }

    // returns the other vertex of the edge NOTE: NEEDS TO BE RE-WRITTEN SO IT RETURNS VERTEX WHEN A WEIGHT
    // IS SENT AND WEIGHT WHEN A VERTEX IS SENT!!!

    // public int other(int vertex) {
    //        if (vertex == v) return w;
    //        else if (vertex == w) return v;
    //        else throw new IllegalArgumentException("Illegal endpoint");
    // }

}