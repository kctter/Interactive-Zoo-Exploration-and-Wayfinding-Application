package com.bennettzhang.zooseeker;

import android.app.Application;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MockLocationTest {
    boolean useLocationServices = false;
    Application application = new Application();
    LocationModel model = new LocationModel(application);
    public static final Coord ENTRANCE_EXIT_GATE = Coord.of(32.734596, -117.149360);
    public static final Coord KOI_FISH = Coord.of(32.72211788245888, -117.15794384136309);
    Coord c1 = new Coord(1.0, 1.0);
    Coord c2 = new Coord(1.0, 1.0);
    List<Coord> route = Coords
            .interpolate(Coords.ENTRANCE_EXIT_GATE, Coords.KOI_FISH, 20)
            .collect(Collectors.toList());


    @Test
    public void locationTest() {
        List<Coord> route = Coords
                .interpolate(Coords.ENTRANCE_EXIT_GATE, Coords.KOI_FISH, 20)
                .collect(Collectors.toList());

        if (!useLocationServices) {
            model.mockRoute(route, 500, TimeUnit.MILLISECONDS);
            System.err.println("location services are off");
        }
    }
}
