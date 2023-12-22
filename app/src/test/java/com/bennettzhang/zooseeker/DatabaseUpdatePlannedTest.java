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
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class DatabaseUpdatePlannedTest {
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
//
//
//    }
//
//    @After
//    public void closeDb() {
//        db.close();
//    }
//
//    @Test
//    public void testPlanned() {
//        //Insert 2 items
//        ZooData.VertexInfo insertedItem1 = new ZooData.VertexInfo("Polar_id", ZooData.VertexInfo.Kind.EXHIBIT, "Polar bear");
//        ZooData.VertexInfo insertedItem2 = new ZooData.VertexInfo("elephant_odyssey", ZooData.VertexInfo.Kind.EXHIBIT, "Elephant Odyssey");
//
//        dao.insert(insertedItem1);
//        dao.insert(insertedItem2);
//
//        insertedItem1.planned = true;
//        dao.update(insertedItem1);
//
//        ZooData.VertexInfo item1 = dao.get(insertedItem1.id);
//        ZooData.VertexInfo item2 = dao.get(insertedItem2.id);
//
//        // Check that the planned value defaults to false and correctly updates to true
//        assertEquals(true, item1.planned);
//        assertEquals(false, item2.planned);
//    }
//
//    @Test
//    public void add2Exhibits() {
//        //Insert 2 items
//        ZooData.VertexInfo insertedItem1 = new ZooData.VertexInfo("kangaroo", ZooData.VertexInfo.Kind.EXHIBIT, "Kangaroo");
//        ZooData.VertexInfo insertedItem2 = new ZooData.VertexInfo("donkey", ZooData.VertexInfo.Kind.EXHIBIT, "Donkey");
//
//        dao.insert(insertedItem1);
//        dao.insert(insertedItem2);
//
//        insertedItem1.planned = true;
//        insertedItem2.planned = true;
//        dao.update(insertedItem1);
//        dao.update(insertedItem2);
//
//        ZooData.VertexInfo item1 = dao.get(insertedItem1.id);
//        ZooData.VertexInfo item2 = dao.get(insertedItem2.id);
//
//        // Check that the planned value defaults to false and correctly updates to true
//        assertEquals(true, item1.planned);
//        assertEquals(true, item2.planned);
//    }
//}
