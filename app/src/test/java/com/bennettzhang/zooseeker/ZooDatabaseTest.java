//package com.bennettzhang.zooseeker;
//
//import android.content.Context;
//
//import androidx.room.Room;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//
//@RunWith(AndroidJUnit4.class)
//public class ZooDatabaseTest {
//    private ZooDataDao dao;
//    private ZooDatabase db;
//
//    @Before
//    public void createDb() {
//        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, ZooDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        dao = db.zooDataDao();
//    }
//
//    @After
//    public void closeDb() {
//        db.close();
//    }
//
//    @Test
//    public void testInsert() {
//        ZooData.VertexInfo insertedItem1 = new ZooData.VertexInfo("Polar_id", ZooData.VertexInfo.Kind.EXHIBIT, "Polar bear");
//        ZooData.VertexInfo insertedItem2 = new ZooData.VertexInfo("elephant_odyssey", ZooData.VertexInfo.Kind.EXHIBIT, "Elephant Odyssey");
//
//        dao.insert(insertedItem1);
//        dao.insert(insertedItem2);
//
//        ZooData.VertexInfo item1 = dao.get(insertedItem1.id);
//        ZooData.VertexInfo item2 = dao.get(insertedItem2.id);
//
//        // Check that these have all been inserted with unique IDs.
//        assertNotEquals(item1.id, item2.id);
//    }
//
//    @Test
//    public void testGet() {
//        ZooData.VertexInfo insertedItem = new ZooData.VertexInfo("elephant_odyssey", ZooData.VertexInfo.Kind.EXHIBIT, "Elephant Odyssey");
//        dao.insert(insertedItem);
//        ZooData.VertexInfo item = dao.get(insertedItem.id);
//
//        assertEquals(insertedItem.id, item.id);
//        assertEquals(insertedItem.kind, item.kind);
//        assertEquals(insertedItem.name, item.name);
//    }
//}
