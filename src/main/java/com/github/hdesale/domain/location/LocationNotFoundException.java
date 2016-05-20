package com.github.hdesale.domain.location;

/**
 * Exception used when a location could not be found by {@link LocationService}.
 *
 * @author Hemant
 */
public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(String message) {
        super(message);
    }
}
