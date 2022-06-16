package me.grupo11;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class GraphBuilder {
    private JSONObject jsonObject;

    private int getIntOrDie(JSONObject o, String key) {
        if (!o.containsKey(key)) {
            throw new IllegalArgumentException("The key: " + key + " is not present!");
        }

        return ((Long) o.get(key)).intValue();
    }

    public GraphBuilder importJson(String path) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader fileReader = new FileReader(path);

        this.jsonObject = (JSONObject) parser.parse(fileReader);
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
