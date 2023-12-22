package com.bennettzhang.zooseeker;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Coords {
    public static final Coord UCSD = Coord.of(32.8801, -117.2340);
    public static final Coord ZOO = Coord.of(32.7353, -117.1490);
    public static final Coord ENTRANCE_EXIT_GATE = Coord.of(32.734596, -117.149360);
    public static final Coord FERN_CANYON = Coord.of(32.733795, -117.176987);
    public static final Coord SIAMANG = Coord.of(32.735851, -117.162894);
    public static final Coord HIPPO = Coord.of(32.746320519009025, -117.16364410510093);
    public static final Coord CROCODILE = Coord.of(32.7463026, -117.166595);
    public static final Coord KOI_FISH = Coord.of(32.72211788245888, -117.15794384136309);

    /**
     * @param p1 first coordinate
     * @param p2 second coordinate
     * @return midpoint between p1 and p2
     */
    public static Coord midpoint(Coord p1, Coord p2) {
        return Coord.of((p1.lat + p2.lat) / 2, (p1.lng + p2.lng) / 2);
    }

    /**
     * @param p1 start coordinate
     * @param p2 end coordinate
     * @param n  number of points between to interpolate.
     * @return a list of evenly spaced points between p1 and p2 (including p1 and p2).
     */
    public static Stream<Coord> interpolate(Coord p1, Coord p2, int n) {
        // Map from i={0, 1, ... n} to t={0.0, 0.1, ..., 1.0} with n divisions.
        ///     t(i; n) = i / n
        // Linear interpolate between l1 and l2 using t:
        //      p(t) = p1 + (p2 - p1) * t

        return IntStream.range(0, n)
                .mapToDouble(i -> (double) i / (double) n)
                .mapToObj(t -> Coord.of(
                        p1.lat + (p2.lat - p1.lat) * t,
                        p1.lng + (p2.lng - p1.lng) * t
                ));
    }
}
