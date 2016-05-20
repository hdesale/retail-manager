package com.github.hdesale.domain.location;

/**
 * Exception used for wrapping an internal exception from {@link LocationService}.
 *
 * @author Hemant
 */
public class LocationServiceException extends RuntimeException {

    public LocationServiceException(String message) {
        super(message);
    }

    public LocationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
