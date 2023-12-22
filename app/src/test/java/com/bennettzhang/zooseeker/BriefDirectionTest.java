//package com.bennettzhang.zooseeker;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//
//import android.content.Context;
//
//import androidx.room.Room;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.jgrapht.Graph;
//import org.jgrapht.GraphPath;
//import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//@RunWith(AndroidJUnit4.class)
//public class BriefDirectionTest {
//    Map<String, ZooData.EdgeInfo> edgeMap;
//    Graph<String, IdentifiedWeightedEdge> g;
//    List<ZooData.VertexInfo> vertexList;
//    Direction direction;
//
//    Context context;
//    ZooDataDao dao;
//    ZooDatabase db;
//
//
//    @Before
//    public void init() {
//        context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, ZooDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        dao = db.zooDataDao();
//        g = ZooData.loadZooGraphJSON(context, "zoo_graph.json");
//        edgeMap = ZooData.loadEdgeInfoJSON(context, "edge_info.json");
//        vertexList = ZooData.loadVertexInfoJSON(context, "node_info.json");
//
//        direction = new Direction();
//        direction.edgeMap = edgeMap;
//        direction.g = g;
//        direction.vertexList = vertexList;
//
//    }
//
//
//    @Test
//    public void testExhibitOrder (){
//        String[] plan = {"koi", "gorilla", "siamang", "hippo"};
//        List<String> exhibitOrder =
//                direction.getDirectionOrder(plan, "entrance_exit_gate", "entrance_exit_gate");
//        ArrayList<String> correctPlan =
//                new ArrayList<String>(Arrays.asList("entrance_exit_gate", "koi", "siamang",
//                        "hippo", "gorilla", "entrance_exit_gate"));
//        for(int i = 0; i < correctPlan.size(); i++){
//            assertEquals(correctPlan.get(i), exhibitOrder.get(i));
//        }
//    }
//
//    @Test
//    public void getBriefDirections() {
//        String[] plan = {"koi", "gorilla", "siamang", "hippo"};
//        List<String> exhibitOrder =
//                direction.getDirectionOrder(plan, "entrance_exit_gate", "entrance_exit_gate");
//        String correctOutput =
//                "  1. Walk 10 meters along Gate Path from 'Entrance and Exit Gate' to 'Front Street / Treetops Way'.\n" +
//                        "   2. Walk 50 meters along Front Street from 'Front Street / Treetops Way' to 'Front Street / Monkey Trail'.\n" +
//                        "   3. Walk 180 meters along Monkey Trail from 'Front Street / Monkey Trail' to 'Capuchin Monkeys'.\n";
//
//                String output = direction.getBriefDirection("entrance_exit_gate","capuchin");
//        assertEquals(output,correctOutput);
//    }
//
//
//
//}
