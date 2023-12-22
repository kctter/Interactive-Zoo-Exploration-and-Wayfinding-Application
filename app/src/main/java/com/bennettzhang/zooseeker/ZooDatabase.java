package com.bennettzhang.zooseeker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;


/**
 * The database of the Zoo. The database will be a singleton object only.
 * <p>
 * The database contains these information:
 * <ul>
 * <li>Exhibit (Vertex)
 * </ul>
 *
 * @see ZooDataDao
 */
@Database(entities = {Exhibit.class}, version = 1, exportSchema = false)
abstract class ZooDatabase extends RoomDatabase {
    private static ZooDatabase singleton;
    
     /**
     * Create a database call zoo.db for this application
     *
     * @param context the current state of the application
     * @return the database of the Zoo
     */
    @NonNull
    private static ZooDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, ZooDatabase.class, "zoo.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() ->
                                getSingleton(context)
                                        .zooDataDao()
                                        .insertAll(
                                                new CopyOnWriteArrayList<>(ZooData.getVertexList()
                                                        .values())));
                    }
                })
                .build();
    }
    
     /**
     * A static instance method to create and ensure it is a singleton object.
     * If the object already exist, it will return the current object rather then create a new one.
     *
     * @param context the current state of the application
     * @return the database of the zoo
     */
    static synchronized ZooDatabase getSingleton(Context context) {
        if (singleton == null) {
            singleton = ZooDatabase.makeDatabase(context);
        }
        return singleton;
    }

    abstract ZooDataDao zooDataDao();

    @VisibleForTesting
    public static void injectTestDatabase(ZooDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }
}
