package com.bennettzhang.zooseeker;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.collect.ImmutableSet;

import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Direction class for displaying the direction of the exhibit
 */
public class Direction {

    String summary = "Summary of Route: \n";

    List<String> exhibitOrder = new ArrayList<>();

    public List<String> getDirectionOrder(Set<String> exhibitsToVisit, String start, String goal) {
        exhibitOrder = ZooData.getGraph().getExhibitVisitOrder(exhibitsToVisit, start);
        return exhibitOrder;
    }


    public List<String> getReplan(int index, String start, String goal) {

        List<String> previousExhibitOrder = exhibitOrder;
        List<String> newExhibitOrder = new ArrayList<>();
        for (int i = 0; i <= index; i++) {
            newExhibitOrder.add(previousExhibitOrder.get(i));
        }
        List<String> futureExhibits = new ArrayList<>();
        for (int j = index + 1; j < previousExhibitOrder.size() - 1; j++) {
            futureExhibits.add(previousExhibitOrder.get(j));
        }

        List<String> rePlanExhibits = getDirectionOrder(
                ImmutableSet.copyOf(futureExhibits),
                start,
                goal);
        for (int k = 0; k < rePlanExhibits.size(); k++) {
            newExhibitOrder.add(rePlanExhibits.get(k));
        }
        return newExhibitOrder;

    }

    public List<String> getReplanSkip(int index, String start, String goal) {
        List<String> previousExhibitOrder = exhibitOrder;
        List<String> exhibitOrder = new ArrayList<>();
        for (int i = 0; i <= index; i++){
            exhibitOrder.add(previousExhibitOrder.get(i));
        }
        List<String> futureExhibits = new ArrayList<>();
        for(int j = index + 2; j < previousExhibitOrder.size() - 1; j++){
            futureExhibits.add(previousExhibitOrder.get(j));
        }
        String arr[] = new String[0];

        List<String> rePlanExhibits = getDirectionOrder(ImmutableSet.copyOf(futureExhibits), start, goal);
        for(int k = 0; k < rePlanExhibits.size(); k++) {
            exhibitOrder.add(rePlanExhibits.get(k));
        }
        return exhibitOrder;

    }

    /**
     * A method of displaying the summary of the direction
     *
     * @param exhibits list of exhibit to be visit
     * @return the summary of the exhibit information
     */
    public String getSummary(List<Exhibit> exhibits) {
        List<String> plan = new ArrayList<>();

        for (int i = 0; i < exhibits.size(); i++) {
            if (exhibits.get(i).planned) {
                Log.e("Exhibits", exhibits.get(i).name);
                plan.add(exhibits.get(i).id);
            }
        }

        // You need to manual add the start exhibit (vertex)
        plan.add("entrance_exit_gate");

        List<String> plannedOrder = getDirectionOrder(new HashSet<>(plan),
                "entrance_exit_gate", "entrance_exit_gate");

        StringBuilder summary = new StringBuilder("");

        if((plannedOrder.size() == 2))
            return "Please go back and add exhibits to Plan";

        //summary.append(ZooData.getVertexList().get(plannedOrder.get(0)).name + "\n");

        for (int i = 1; i < plannedOrder.size() -1; i++) {
            String name = ZooData.getVertexList().get(plannedOrder.get(i)).name;
            Log.e("Exhibit name", name);
            summary.append(name + " - ");
            double weight = ZooData.getGraph().zooShortestPath.getPathWeight("entrance_exit_gate", plannedOrder.get(i));
            summary.append(weight + "ft \n");
        }

        //summary.append(ZooData.getVertexList().get(plannedOrder.get(plannedOrder.size() -1)).name);
        return summary.toString();
    }

    /**
     * Helper method to display all the direction og the exhibit visiting infromation
     *
     * @param source the start of the exhibit (vertex) to be visit
     * @param sink the goal of the exhibit (vertex) to be visit
     * @param groupAnimals
     * @return a string of direction of the visiting order
     */
    public String getDetailedDirection(String source, String sink,List<String> groupAnimals) {
        int step = 1; // step tracker

        Map<String, Exhibit> vertexList = ZooData.getVertexList();
        Zoo graph = ZooData.getGraph();

        GraphPath<String, IdentifiedWeightedEdge> path = graph.zooShortestPath.getPath(source, sink);

        StringBuilder rote = new StringBuilder();

        // Looping the Vertex in pair
        List<String> visitOrder = path.getVertexList();
        for (int i = 0; i < path.getLength(); i++) {
            String begin = visitOrder.get(i);
            String end = visitOrder.get(i + 1);

            rote.append(printDirection(path.getEdgeList().get(i), graph.zooGraph.getEdgeWeight(path.getEdgeList().get(i)), step, vertexList.get(begin).name, vertexList.get(end).name));
            summary += vertexList.get(end) + " - " + graph.zooShortestPath.getPathWeight(end, begin) + " ft \n";
            // if the end vertex is a group exhibit
            if(vertexList.get(end).isGroup() && i == path.getLength()-1){
                rote.append(getGroupMembers(end,groupAnimals));
            }
            step++;
        }
        return rote.toString();
    }

    private String getGroupMembers(String groupName,List<String> groupAnimals) {
        List<String> animals = new ArrayList<String>();
        Map<String,Exhibit> vertexList = ZooData.getVertexList();

        for (Exhibit animal : vertexList.values()) {
            if (animal.hasGroup()) {
                if (animal.groupId.equals(groupName)){
                    animals.add(animal.name);
                }
            }
        }
        String groupMembers = "Find ";
        if(animals.size() > 2) {
            for (int j = 0; j < animals.size() - 1; j++) {
                groupMembers += animals.get(j) + ", ";
            }
            groupMembers += "and " + animals.get(animals.size() - 1) + " inside.\n";
        } else if(animals.size() == 2) {
            groupMembers += animals.get(0) + " and " + animals.get(1) + " inside. \n";
        } else {
            groupMembers += animals.get(0) + " inside.\n";
        }
        return groupMembers;
    }
    
    /**
     * Helper method to display all the direction og the exhibit visiting information
     * Brief direction will combine all the continuous street name into one step
     *
     * @param source the start of the exhibit (vertex) to be visit
     * @param sink the goal of the exhibit (vertex) to be visit
     * @param groupAnimals
     * @return a string of brief direction of the visiting order
     */
    public String getBriefDirection(String source, String sink,List<String> groupAnimals) {
        Map<String, Exhibit> exhibitMap = ZooData.getVertexList();
        Map<String, Trail> trailMap = ZooData.getEdgeMap();
        Zoo zooGraph = ZooData.getGraph();

        int step = 1; // step tracker

        GraphPath<String, IdentifiedWeightedEdge> path = zooGraph.zooShortestPath.getPath(source, sink);

        StringBuilder rote = new StringBuilder();

        // Looping the Vertex in pair
        List<String> visitOrder = path.getVertexList();
        for (int i = 0; i < path.getLength(); i++) {
            String begin = visitOrder.get(i);
            String end = visitOrder.get(i + 1);

            // Continue looking for the next end to check if the street name are the same
            while (i < path.getLength()- 1) {
                // If the street name does not match the previous one
                if (!trailMap.get(path.getEdgeList().get(i).getId()).street
                        .equals(trailMap.get(path.getEdgeList().get(i + 1).getId()).street)) {
                    break;
                }
                end = visitOrder.get(i + 2);
                i++;
            }

            rote.append(printDirection(path.getEdgeList().get(i), zooGraph.zooShortestPath.getPathWeight(begin, end), step, exhibitMap.get(begin).name, exhibitMap.get(end).name));
            summary += exhibitMap.get(end) + " - " + zooGraph.zooShortestPath.getPathWeight(begin, end) + " ft \n";
            // if the end vertex is a group exhibit
            if(exhibitMap.get(end).isGroup() && path.getLength()-1 == i){
                rote.append(getGroupMembers(end,groupAnimals));
            }
            step++;
        }
        return rote.toString();
    }
    
    /**
     * Helper method to pint the direction of the start and goal exhibit
     *
     * @param p the edge information between the start and the goal exhibit
     * @param weightEdge distance of the edge
     * @param step a counter of the direction
     * @param beginName the start of the exhibit
     * @param endName the goal of the exhibit
     * @return a string formatted direction information
     */
    public String printDirection(IdentifiedWeightedEdge p, double weightEdge, int step, String beginName,
                                 String endName) {
        Map<String, Trail> trailMap = ZooData.getEdgeMap();
        return String.format(Locale.getDefault(), "  %d. Walk %.0f ft along %s from '%s' to '%s'.\n"
                , step
                , weightEdge
                , trailMap.get(p.getId()).street
                , beginName
                , endName);
    }

    public String getSummary(){
        return summary;
    }

    public List<Coord> getExhibitCoord() {
        List<String> vertices = exhibitOrder;
        List<Coord> exhibitCoord = new ArrayList<>();
        for (String vertex: vertices) {


            Exhibit e = ZooData.getVertexList().get(vertex);
            exhibitCoord.add(new Coord(e.lat, e.lng));
        }
        return exhibitCoord;
    }
}
