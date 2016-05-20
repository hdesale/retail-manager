package com.github.hdesale.domain.location;

/**
 * Location service for<br>
 *     - finding the geo location from an address<br>
 *     - calculating distance between two geo locations<br>
 *
 * @author Hemant
 * @see GoogleLocationService
 */
public interface LocationService {

    /**
     * Finds a location from an address.
     *
     * @param address string
     * @return location containing the latitude and longitude details
     */
    Location findLocation(String address);

    /**
     * Calculates a distance between two geo locations.
     *
     * @param first location
     * @param second second location
     * @return distance in meters
     */
    double calculateDistance(Location first, Location second);
}
