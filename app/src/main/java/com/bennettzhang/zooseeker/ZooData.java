package com.bennettzhang.zooseeker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ZooData class contain the core of the zoo information
 * <p>
 * ZooData contains these information:
 * <ul>
 * <li>Trail (Edge)
 * <li>Exhibit (Vertex)
 * <li>Zoo (Graph)
 * </ul>
 */
public class ZooData {
    private static Map<String, Exhibit> exhibitMap;
    private static Map<String, Trail> trailMap;
    //    private static Graph<String, IdentifiedWeightedEdge> graph;
    private static Zoo zooGraph;


    /**
     * Initialize the zooData by reading the Trail, Exhibit and Zoo json file
     *
     * @param context the current state of the application
     */
    public static void initZooData(Context context) {
        exhibitMap = loadVertexInfoJSON(context, "exhibit_info.json");
        trailMap = loadEdgeInfoJSON(context, "trail_info.json");
//        graph = loadZooGraphJSON(context, "zoo_graph.json");
        zooGraph = new Zoo(loadZooGraphJSON(context, "zoo_graph.json"));
    }

    /**
     * Helper method to read the exhibit json information
     *
     * @param context the current state of the application
     * @param path    a string path of the json file
     * @return a map of the exhibit where the key is the id and the value is the exhibit object
     */
    @Nullable
    public static Map<String, Exhibit> loadVertexInfoJSON(@NonNull Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Exhibit>>() {
            }.getType();
            List<Exhibit> exhibitList = gson.fromJson(reader, type);

            return exhibitList
                    .stream()
                    .collect(Collectors.toMap(vertex -> vertex.id, datum -> datum));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to read the trail json information
     *
     * @param context the current state of the application
     * @param path    a string path of the json file
     * @return a map of the trail where the key is the id and the value is the trail object
     */
    @Nullable
    public static Map<String, Trail> loadEdgeInfoJSON(@NonNull Context context, String path) {
        try {
            InputStream inputStream = context.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            Type type = new TypeToken<List<Trail>>() {
            }.getType();
            List<Trail> trailList = gson.fromJson(reader, type);

            return trailList
                    .stream()
                    .collect(Collectors.toMap(edge -> edge.id, datum -> datum));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to read the zoo graph json information
     *
     * @param context the current state of the application
     * @param path    a string path of the json file
     * @return a map of the exhibit where the key is the id
     * and the value is the trail weight (distance) object
     */
    @Nullable
    public static Graph<String, IdentifiedWeightedEdge> loadZooGraphJSON(
            @NonNull Context context,
            String path) {
        try {
            // Create an empty graph to populate.
            Graph<String, IdentifiedWeightedEdge> graph =
                    new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

            // Create an importer that can be used to populate our empty graph.
            JSONImporter<String, IdentifiedWeightedEdge> importer = new JSONImporter<>();

            // We don't need to convert the vertices in the graph, so we return them as is.
            importer.setVertexFactory(vertex -> vertex);

            // We need to make sure we set the IDs on our edges from the 'id' attribute.
            // While this is automatic for vertices, it isn't for edges. We keep the
            // definition of this in the IdentifiedWeightedEdge class for convenience.
            importer.addEdgeAttributeConsumer(IdentifiedWeightedEdge::attributeConsumer);

            // On Android, you would use context.getAssets().open(path) here like in Lab 5.
            InputStream inputStream = context.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);

            // And now we just import it!
            importer.importGraph(graph, reader);

            return graph;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Getter method to get the current exhibitMap object
     *
     * @return a map of the exhibit where the key is the id and the value is the exhibit object
     */
    static Map<String, Exhibit> getVertexList() {
        return exhibitMap;
    }

    /**
     * Getter method to get the current trailMap object
     *
     * @return a map of the trail where the key is the id and the value is the trail object
     */
    static Map<String, Trail> getEdgeMap() {
        return trailMap;
    }
    
    /**
     * Getter method to get the current zooGraph object
     *
     * @return a map of the exhibit where the key is the id
     * and the value is the trail weight (distance) object
     */
    static Zoo getGraph() {
        return zooGraph;
    }

}
