package com.bennettzhang.zooseeker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;

@Entity(tableName = "trails")
public class Trail {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @NonNull
    final String id;

    @ColumnInfo(name = "street")
    @SerializedName("street")
    @NonNull
    final String street;

    public Trail(@NonNull String id,
                 @NonNull String street) {
        this.id = id;
        this.street = street;
    }

    public static List<Trail> fromJson(Reader infoReader) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Trail>>() {
        }.getType();
        return gson.fromJson(infoReader, type);
    }

    public static void toJson(List<Trail> info, Writer writer) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Trail>>() {
        }.getType();
        gson.toJson(info, type, writer);
        writer.flush();
        writer.close();
    }
}
