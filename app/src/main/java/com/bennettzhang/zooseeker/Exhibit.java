package com.bennettzhang.zooseeker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;

@Entity(tableName = "exhibit")
public class Exhibit {
    /**
     * Load ZooNode's from a JSON file (such as vertex_info.json).
     *
     * @param infoReader a reader from which to read the JSON input.
     * @return a list
     */
    public static List<Exhibit> fromJson(Reader infoReader) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Exhibit>>() {
        }.getType();
        return gson.fromJson(infoReader, type);
    }

    public static void toJson(List<Exhibit> info, Writer writer) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Exhibit>>() {
        }.getType();
        gson.toJson(info, type, writer);
        writer.flush();
        writer.close();
    }

    public enum Kind {
        // The SerializedName annotation tells GSON how to convert
        // from the strings in our JSON to this Enum.
        @SerializedName("gate") GATE,
        @SerializedName("exhibit") EXHIBIT,
        @SerializedName("intersection") INTERSECTION,
        @SerializedName("exhibit_group") EXHIBIT_GROUP;
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @NonNull
    String id;

    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    @Nullable
    String groupId;

    @ColumnInfo(name = "kind")
    @SerializedName("kind")
    @NonNull
    Kind kind;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @NonNull
    String name;

    @ColumnInfo(name = "tags")
    @SerializedName("tags")
    @TypeConverters({Converters.class})
    @NonNull
    List<String> tags;

    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    Double lat;

    @ColumnInfo(name = "lng")
    @SerializedName("lng")
    Double lng;

    boolean planned = false;

    public boolean isExhibit() {
        return kind.equals(Kind.EXHIBIT);
    }

    public boolean isIntersection() {
        return kind.equals(Kind.INTERSECTION);
    }

    public boolean isGroup() {
        return kind.equals(Kind.EXHIBIT_GROUP);
    }

    public boolean hasGroup() {
        return groupId != null;
    }

    public Exhibit(@NonNull String id,
                   @Nullable String groupId,
                   @NonNull Kind kind,
                   @NonNull String name,
                   @NonNull List<String> tags,
                   @Nullable Double lat,
                   @Nullable Double lng) {
        this.id = id;
        this.groupId = groupId;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
        this.lat = lat;
        this.lng = lng;

        if (!this.hasGroup() && (lat == null || lng == null)) {
            throw new RuntimeException("Nodes must have a lat/long unless they are grouped.");
        }
    }

}
