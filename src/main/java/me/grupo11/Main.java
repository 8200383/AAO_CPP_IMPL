package me.grupo11;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        Graph graph = new GraphBuilder()
                .importJson("semi-eulerian.json")
                .buildUndirectedGraph();

        System.out.println(graph.plot(false));
    }
}