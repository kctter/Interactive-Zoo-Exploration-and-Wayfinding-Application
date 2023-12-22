package com.bennettzhang.zooseeker;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraManyToManyShortestPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(AndroidJUnit4.class)
public class ZooOptimizedRouteTest {

    @Test
    public void complicatedDirectionTest() {
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


//        System.out.printf("The shortest path from '%s' to '%s' is:\n", start, goal);
//
//        int i = 1;
//        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
//            System.out.printf("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
//                    i,
//                    g.getEdgeWeight(e),
//                    eInfo.get(e.getId()).street,
//                    vInfo.get(g.getEdgeSource(e).toString()).name,
//                    vInfo.get(g.getEdgeTarget(e).toString()).name);
//            i++;
//        }


        String[] arr = {"arctic_foxes",
                "elephant_odyssey",
                "entrance_exit_gate",
                "entrance_plaza",
                "gators",
                "gorillas",
                "lions"};
        Set<String> a = new HashSet<>();
        Collections.addAll(a, arr);

//        String[] arrs = {"entrance_exit_gate"};
//        Set<String> b = new HashSet<>();
//        Collections.addAll(b, arrs);

        DijkstraManyToManyShortestPaths<String, IdentifiedWeightedEdge> manyShortestPaths = new DijkstraManyToManyShortestPaths<>(g);
        ManyToManyShortestPathsAlgorithm.ManyToManyShortestPaths<String, IdentifiedWeightedEdge> shortestPaths
                = manyShortestPaths.getManyToManyPaths(a, a);

//        System.out.println(shortestPaths.getPath("gorillas", "lions").getWeight());


        String[] arrs = {"arctic_foxes",
                "elephant_odyssey",

                "entrance_plaza",
                "gators",
                "gorillas",
                "lions"};
        List<String> b = new ArrayList<>();
        Collections.addAll(b, arrs);

        String source = "entrance_exit_gate";
        String temp = "";
        double distance = Double.MAX_VALUE;
        while (b.size() != 0) {

            temp = "";
            distance = Double.MAX_VALUE;
            for (String destination : b) {
                double newDistance = shortestPaths.getPath(source, destination).getWeight();
                if (newDistance < distance) {
                    temp = destination;
                    distance = newDistance;
                }

            }

            System.out.println(shortestPaths.getPath(source, temp));
            source = temp;
            b.remove(temp);
        }

        System.out.println(shortestPaths.getPath(source, "entrance_exit_gate"));

//        System.out.println(Arrays.stream(combinations(arrs)).toArray().toString());
//        combinations(arrs);
       */


    }

    @Test
    public void elephantOdysseyTest() {
        /*
        // "source" and "sink" are graph terms for the start and end
        String start = "entrance_exit_gate";
        String goal = "elephant_odyssey";

        // 1. Load the graph...
        // Create an empty graph to populate.
        Graph<String, IdentifiedWeightedEdge> g = new DefaultDirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

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

        Set<IdentifiedWeightedEdge> s = g.edgeSet();
        List<IdentifiedWeightedEdge> list = new ArrayList<>(s);

        for (IdentifiedWeightedEdge edge : list) {
//            System.out.println(g.getEdgeSource(edge) + " " + g.getEdgeWeight(edge) + " " + g.getEdgeTarget(edge));
            IdentifiedWeightedEdge e = g.addEdge(g.getEdgeTarget(edge), g.getEdgeSource(edge));
            e.setId("R-"+edge.getId());
            g.setEdgeWeight(e, g.getEdgeWeight(edge));


        }

        String[] arr = {"arctic_foxes",
                "elephant_odyssey",
                "entrance_exit_gate",
                "entrance_plaza",
                "gators",
                "gorillas",
                "lions"};
        Set<String> a = new HashSet<>();
        Collections.addAll(a, arr);

//        String[] arrs = {"entrance_exit_gate"};
//        Set<String> b = new HashSet<>();
//        Collections.addAll(b, arrs);

        DijkstraManyToManyShortestPaths<String, IdentifiedWeightedEdge> manyShortestPaths = new DijkstraManyToManyShortestPaths<>(g);
        ManyToManyShortestPathsAlgorithm.ManyToManyShortestPaths<String, IdentifiedWeightedEdge> shortestPaths
                = manyShortestPaths.getManyToManyPaths(a, a);

//        System.out.println(shortestPaths.getPath("gorillas", "lions").getWeight());


        String[] arrs = {"arctic_foxes",
                "elephant_odyssey",

                "entrance_plaza",
                "gators",
                "gorillas",
                "lions"};
        List<String> b = new ArrayList<>();
        Collections.addAll(b, arrs);

        String source = "entrance_exit_gate";
        String temp = "";
        double distance = Double.MAX_VALUE;
        int i = 1;
        while (b.size() != 0) {

            temp = "";
            distance = Double.MAX_VALUE;
            for (String destination : b) {
                double newDistance = shortestPaths.getPath(source, destination).getWeight();
                if (newDistance < distance) {
                    temp = destination;
                    distance = newDistance;
                }

            }

//            shortestPaths.getPath(source, temp).toString();
            GraphPath<String, IdentifiedWeightedEdge> path = shortestPaths.getPath(source, temp);


//            System.out.println(shortestPaths.getPath(source, temp));
            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                System.out.printf("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                        i,
                        g.getEdgeWeight(e),
                        g.getEdge(g.getEdgeSource(e), g.getEdgeTarget(e)).getId(),
                        g.getEdgeSource(e).toString(),
                        g.getEdgeTarget(e).toString());
                i++;
            }
            source = temp;
            b.remove(temp);
        }

        temp = "entrance_exit_gate";
        GraphPath<String, IdentifiedWeightedEdge> path = shortestPaths.getPath(source, temp);


//            System.out.println(shortestPaths.getPath(source, temp));
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            System.out.printf("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                    i,
                    g.getEdgeWeight(e),
                    g.getEdge(g.getEdgeSource(e), g.getEdgeTarget(e)).getId(),
                    g.getEdgeSource(e).toString(),
                    g.getEdgeTarget(e).toString());
            i++;
        }

    */
    }

}
