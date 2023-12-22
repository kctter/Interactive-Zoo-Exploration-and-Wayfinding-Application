package com.bennettzhang.zooseeker;


import androidx.annotation.NonNull;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.HamiltonianCycleAlgorithm;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.CHManyToManyShortestPaths;
import org.jgrapht.alg.tour.NearestNeighborHeuristicTSP;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;
import java.util.Set;

/**
 * Zoo class holding the Graph information
 */
class Zoo {
    Graph<String, IdentifiedWeightedEdge> zooGraph;
    Graph<String, IdentifiedWeightedEdge> zooCompleteGraph;

    ShortestPathAlgorithm<String, IdentifiedWeightedEdge> zooShortestPath;

     /**
     * Construction to initialize the zoo graph information
     * It will also initialize a shortest path between all exhibits (vertices)
     * and a complete graph of the TSP problem
     *
     * @param zooGraph graph of the zoo
     */
    Zoo(Graph<String, IdentifiedWeightedEdge> zooGraph) {
        this.zooGraph = zooGraph;
        this.zooShortestPath = getAllShortestPaths(this.zooGraph);
        this.zooCompleteGraph = getCompleteGraph(this.zooGraph);
    }

     /**
     * Helper method to construct a complete graph
     * It will initialize a new graph object
     * and calculate all the shortest distance between each vertices
     * Then it will construct the complete graph
     * by getting all 2 pair combination of the vertices and it weight
     *
     * @param graph the graph that need to turn it into a complete graph
     * @return a graph (complete graph)
     */
    Graph<String, IdentifiedWeightedEdge> getCompleteGraph(
            @NonNull Graph<String, IdentifiedWeightedEdge> graph) {
        // Generate a new empty graph
        Graph<String, IdentifiedWeightedEdge> completeGraph =
                new SimpleWeightedGraph<>(IdentifiedWeightedEdge.class);

        // Calculate all the combinations of the graph vertex
        Set<Set<String>> combinations = Sets.combinations(graph.vertexSet(), 2);


        // Put each combination and weight back to the empty graph
        combinations.forEach(combination ->
                Graphs.addEdgeWithVertices(
                        completeGraph,
                        Iterables.get(combination, 0),
                        Iterables.get(combination, 1),
                        zooShortestPath.getPathWeight(
                                Iterables.get(combination, 0),
                                Iterables.get(combination, 1))));

        return completeGraph;
    }

     /**
     * Construct a subgraph of the graph
     * A subgraph is a graph whose vertex set and edges set are subset of those of.
     * This is a helper method aid for calculation the TSP problem
     * It will get it from the complete zoo graph
     *
     * @param vertices set of vertices that want to be the subset of
     * @return a subgraph of the vertices of the complete graph
     */
    Graph<String, IdentifiedWeightedEdge> getSubGraph(Set<String> vertices) {
        return new AsSubgraph<>(zooCompleteGraph, vertices);
    }
    
     /**
     * Core algorithm of calculating the TSP shortest path problem
     * It is using the JGraph NearestNeighborHeuristicTSP to calculate the TSP
     *
     * @param graph a complete graph
     * @param sourceVertex the starting vertex
     * @return a path where the start is the sourceVertex
     */
    GraphPath<String, IdentifiedWeightedEdge> getTour(
            Graph<String, IdentifiedWeightedEdge> graph,
            String sourceVertex) {
        HamiltonianCycleAlgorithm<String, IdentifiedWeightedEdge> tsp =
                new NearestNeighborHeuristicTSP<>(sourceVertex);
        return tsp.getTour(graph);
    }

     /**
     * Core algorithm of calculating the shortest path problem between each vertices
     * It is using the JGraph CHManyToManyShortestPaths to calculate the shortest path
     *
     * @param graph a graph
     * @return a shortest path between each vertices
     */
    ShortestPathAlgorithm<String, IdentifiedWeightedEdge> getAllShortestPaths(
            Graph<String, IdentifiedWeightedEdge> graph) {
        return new CHManyToManyShortestPaths<>(graph);
    }

     /**
     * Helper method to get all the exhibit (vertex) visit order from the TSP problem
     *
     * @param exhibitsToVisit a set of exhibit (vertices) to be visit
     * @param start the start of the exhibit (vertex)
     * @return a list of ordered exhibit to visit
     */
    List<String> getExhibitVisitOrder(Set<String> exhibitsToVisit, String start) {
        return getTour(getSubGraph(exhibitsToVisit), start).getVertexList();
    }

}
