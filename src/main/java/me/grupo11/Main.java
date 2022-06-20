package me.grupo11;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        GraphADT graph = new GraphBuilder()
                .importJson("semi-eulerian.json")
                .buildUndirectedGraph();

        System.out.println(graph.isEulerian());
        System.out.println(graph.isConnected());
        System.out.println(graph.plot(false));
        System.out.println(graph.getSingleNodes().toString());

        System.out.println("------------ Shortest Path ------------");
        Iterator<Integer> shortestPath = graph.iteratorShortestPath(0, 4);
        while (shortestPath.hasNext()) {
            System.out.println(shortestPath.next());
        }
        System.out.println("-------------------------------------------------");

        System.out.println("------------ Eulerian Trail Or Cycle ------------");
        Iterator<Integer> eulerianCycle = graph.iteratorEulerianTrailOrCycle();
        while (eulerianCycle.hasNext()) {
            System.out.println(eulerianCycle.next());
        }
        System.out.println("-------------------------------------------------");

        System.out.println(graph);
    }
}