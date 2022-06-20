package me.grupo11;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GraphBuilder {
    private JSONObject jsonObject;

    private int getIntOrDie(JSONObject o, String key) {
        if (!o.containsKey(key)) {
            throw new IllegalArgumentException("The key: " + key + " is not present!");
        }

        return ((Long) o.get(key)).intValue();
    }

    public GraphBuilder importJson(String path) throws ParseException, IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new FileNotFoundException(path);
        }

        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        this.jsonObject = (JSONObject) new JSONParser().parse(reader);

        return this;
    }

    public Graph buildUndirectedGraph() {
        Graph graph = new UndirectedGraph(this.getIntOrDie(this.jsonObject, "post-offices") + 1);

        JSONArray paths = (JSONArray) this.jsonObject.get("paths");

        for (Object o : paths) {
            JSONObject path = (JSONObject) o;

            graph.addEdge(
                    this.getIntOrDie(path, "from"),
                    this.getIntOrDie(path, "to"),
                    this.getIntOrDie(path, "cost")
            );
        }

        return graph;
    }
}
