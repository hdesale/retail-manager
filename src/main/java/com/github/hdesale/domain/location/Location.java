package com.github.hdesale.domain.location;

import net.jcip.annotations.Immutable;

import java.util.Objects;

/**
 * This is a value object which represents a location using latitude and longitude.
 * <p>
 * Two locations are equal only if <tt>loc1.lat == loc2.lat && loc1.lng == loc2.lng</tt>
 *
 * @author Hemant
 */
@Immutable
public class Location {

    private final double lat;

    private final double lng;

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.lat, lat) == 0 &&
                Double.compare(location.lng, lng) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }
}
