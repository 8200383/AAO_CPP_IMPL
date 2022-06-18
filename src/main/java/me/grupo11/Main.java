package me.grupo11;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        GraphADT graph = new GraphBuilder()
                .importJson("eulerian.json")
                .buildUndirectedGraph();

        System.out.println(graph);
    }
}