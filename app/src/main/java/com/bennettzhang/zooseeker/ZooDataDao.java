package com.bennettzhang.zooseeker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * A Data Access Object (DAO) interface that provides access to the underlying database.
 */
@Dao
interface ZooDataDao {
    @Insert
    void insert(Exhibit exhibit);

    @Insert
    void insertAll(List<Exhibit> exhibitList);

//    @Query("SELECT zooData.vertexInfo.name + 1 FROM vertex_table ORDER BY `name` DESC LIMIT 1")
//    int getOrderForAppend();

    @Query("SELECT * FROM exhibit WHERE `id`=:id")
    Exhibit get(String id);

    @Query("SELECT * FROM exhibit ORDER BY name")
    List<Exhibit> getAll();

    @Query("SELECT * FROM exhibit ORDER BY name")
    LiveData<List<Exhibit>> getAllLive();

    @Update
    int update(Exhibit exhibit);

    @Update
    int updateAll(List<Exhibit> exhibitList);

    @Delete
    int delete(Exhibit exhibit);
}
