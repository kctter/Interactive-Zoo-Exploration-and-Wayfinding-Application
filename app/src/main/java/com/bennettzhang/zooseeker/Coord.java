package com.bennettzhang.zooseeker;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
//import com.google.common.base.Objects;


public class Coord {
    final double lat;
    final double lng;

    public Coord(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public static Coord of(double lat, double lng) {
        return new Coord(lat, lng);
    }

    public static Coord fromLatLng(LatLng latLng) {
        return Coord.of(latLng.latitude, latLng.longitude);
    }

    public static Coord fromLocation(Location location) {
        return Coord.of(location.getLatitude(), location.getLongitude());
    }

    public LatLng toLatLng() {
        return new LatLng(lat, lng);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coord coord = (Coord) o;
        return Double.compare(coord.lat, lat) == 0 && Double.compare(coord.lng, lng) == 0;
    }
/*
    @Override
    public int hashCode() {
        return Objects.hashCode(lat, lng);
    }

 */

    @NonNull
    @Override
    public String toString() {
        return String.format("Coord{lat=%s, lng=%s}", lat, lng);
    }
}
