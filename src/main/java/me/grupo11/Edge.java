package me.grupo11;

public class Edge<V extends Integer> implements Comparable<Edge<V>> {
    public V from;

    public V to;
    public int weight;
    public boolean isUsed;

    public Edge(V from, V to) {
        this.from = from;
        this.to = to;
        this.weight = -1;
        this.isUsed = false;
    }

    public Edge(V from, V to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.isUsed = false;
    }

    @Override
    public int compareTo(Edge<V> edge) {
        return edge.from.equals(this.from) && edge.to.equals(this.to) ? 0 : -1;
    }

    // returns the other vertex of the edge NOTE: NEEDS TO BE RE-WRITTEN SO IT RETURNS VERTEX WHEN A WEIGHT
    // IS SENT AND WEIGHT WHEN A VERTEX IS SENT!!!

    // public int other(int vertex) {
    //        if (vertex == v) return w;
    //        else if (vertex == w) return v;
    //        else throw new IllegalArgumentException("Illegal endpoint");
    // }

}