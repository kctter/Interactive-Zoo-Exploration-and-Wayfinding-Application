package com.bennettzhang.zooseeker;


import android.content.res.AssetManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraManyToManyShortestPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.json.JSONImporter;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(AndroidJUnit4.class)
public class ZooDirectionTest {

    @Test
    public void basicDirectionTest() {
        /*
        // "source" and "sink" are graph terms for the start and end
        String start = "entrance_exit_gate";
        String goal = "elephant_odyssey";

        // 1. Load the graph...
        // Create an empty graph to populate.
        Graph<String, IdentifiedWeightedEdge> g = new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

        // Create an importer that can be used to populate our empty graph.
        JSONImporter<String, IdentifiedWeightedEdge> importer = new JSONImporter<>();

        // We don't need to convert the vertices in the graph, so we return them as is.
        importer.setVertexFactory(v -> v);

        // We need to make sure we set the IDs on our edges from the 'id' attribute.
        // While this is automatic for vertices, it isn't for edges. We keep the
        // definition of this in the IdentifiedWeightedEdge class for convenience.
        importer.addEdgeAttributeConsumer(IdentifiedWeightedEdge::attributeConsumer);

        // On Android, you would use context.getAssets().open(path) here like in Lab 5.
        // load path
        InputStream graphInputStream = getClass().getClassLoader().getResourceAsStream("sample_zoo_graph.json");
        Reader graphReader = new InputStreamReader(graphInputStream);

        // And now we just import it!
        importer.importGraph(g, graphReader);

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, goal);

        // 2. Load the information about our nodes and edges...
        // load vertices
        InputStream vertexInputStream = getClass().getClassLoader().getResourceAsStream("sample_node_info.json");
        Reader vertexReader = new InputStreamReader(vertexInputStream);

        Gson vertexGson = new Gson();
        Type vertexType = new TypeToken<List<ZooData.VertexInfo>>() {
        }.getType();
        List<ZooData.VertexInfo> zooVerticesData = vertexGson.fromJson(vertexReader, vertexType);

        Map<String, ZooData.VertexInfo> vInfo = zooVerticesData
                .stream()
                .collect(Collectors.toMap(v -> v.id, datum -> datum));


        // load edges
        InputStream edgeInputStream = getClass().getClassLoader().getResourceAsStream("sample_edge_info.json");
        Reader edgeReader = new InputStreamReader(edgeInputStream);

        Gson edgeGson = new Gson();
        Type edgeType = new TypeToken<List<ZooData.EdgeInfo>>() {
        }.getType();
        List<ZooData.EdgeInfo> zooEdgesData = edgeGson.fromJson(edgeReader, edgeType);

        Map<String, ZooData.EdgeInfo> eInfo = zooEdgesData
                .stream()
                .collect(Collectors.toMap(v -> v.id, datum -> datum));


        System.out.printf("The shortest path from '%s' to '%s' is:\n", start, goal);

        int i = 1;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            System.out.printf("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                    i,
                    g.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    vInfo.get(g.getEdgeSource(e).toString()).name,
                    vInfo.get(g.getEdgeTarget(e).toString()).name);
            i++;
        }
        */

    }

    @Test
    public void basicDirectionTest2() {
        /*
        // "source" and "sink" are graph terms for the start and end
        String start = "entrance_exit_gate";
        String goal = "entrance_exit_gate";

        // 1. Load the graph...
        // Create an empty graph to populate.
        Graph<String, IdentifiedWeightedEdge> g = new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

        // Create an importer that can be used to populate our empty graph.
        JSONImporter<String, IdentifiedWeightedEdge> importer = new JSONImporter<>();

        // We don't need to convert the vertices in the graph, so we return them as is.
        importer.setVertexFactory(v -> v);

        // We need to make sure we set the IDs on our edges from the 'id' attribute.
        // While this is automatic for vertices, it isn't for edges. We keep the
        // definition of this in the IdentifiedWeightedEdge class for convenience.
        importer.addEdgeAttributeConsumer(IdentifiedWeightedEdge::attributeConsumer);

        // On Android, you would use context.getAssets().open(path) here like in Lab 5.
        // load path
        InputStream graphInputStream = getClass().getClassLoader().getResourceAsStream("sample_zoo_graph.json");
        Reader graphReader = new InputStreamReader(graphInputStream);

        // And now we just import it!
        importer.importGraph(g, graphReader);

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, goal);

        // 2. Load the information about our nodes and edges...
        // load vertices
        InputStream vertexInputStream = getClass().getClassLoader().getResourceAsStream("sample_node_info.json");
        Reader vertexReader = new InputStreamReader(vertexInputStream);

        Gson vertexGson = new Gson();
        Type vertexType = new TypeToken<List<ZooData.VertexInfo>>() {
        }.getType();
        List<ZooData.VertexInfo> zooVerticesData = vertexGson.fromJson(vertexReader, vertexType);

        Map<String, ZooData.VertexInfo> vInfo = zooVerticesData
                .stream()
                .collect(Collectors.toMap(v -> v.id, datum -> datum));


        // load edges
        InputStream edgeInputStream = getClass().getClassLoader().getResourceAsStream("sample_edge_info.json");
        Reader edgeReader = new InputStreamReader(edgeInputStream);

        Gson edgeGson = new Gson();
        Type edgeType = new TypeToken<List<ZooData.EdgeInfo>>() {
        }.getType();
        List<ZooData.EdgeInfo> zooEdgesData = edgeGson.fromJson(edgeReader, edgeType);

        Map<String, ZooData.EdgeInfo> eInfo = zooEdgesData
                .stream()
                .collect(Collectors.toMap(v -> v.id, datum -> datum));


        System.out.printf("The shortest path from '%s' to '%s' is:\n", start, goal);

        int i = 1;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            System.out.printf("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                    i,
                    g.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    vInfo.get(g.getEdgeSource(e).toString()).name,
                    vInfo.get(g.getEdgeTarget(e).toString()).name);
            i++;
        }
       */
    }

}
