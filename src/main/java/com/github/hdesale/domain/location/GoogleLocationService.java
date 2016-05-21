package com.github.hdesale.domain.location;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.glassfish.jersey.client.ClientConfig;
import org.hibernate.validator.constraints.NotBlank;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static com.google.common.cache.CacheBuilder.newBuilder;
import static java.lang.Math.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.UriBuilder.fromUri;

/**
 * Implementation of <tt>LocationService</tt> using Google maps geo-coding api.
 *
 * @author Hemant
 * @see <a href="https://developers.google.com/maps/documentation/geocoding/start">
 * Google Maps Geocoding API Documentaion</a>
 */
@Service("googleLocationService")
public class GoogleLocationService implements LocationService {

    private static final Logger log = LoggerFactory.getLogger(GoogleLocationService.class);

    @Value("${google.maps.geo-coding.root}")
    private String apiRoot;

    @Value("${google.maps.geo-coding.path}")
    private String apiPath;

    @Value("${google.maps.geo-coding.param.address}")
    private String addressParam;

    @Value("${google.maps.geo-coding.param.key}")
    private String keyParam;

    @Value("${google.maps.geo-coding.api.key}")
    private String apiKey;

    private Client restClient;

    private LoadingCache<String, Location> locationCache;

    @PostConstruct
    public void init() throws KeyManagementException, NoSuchAlgorithmException {
        restClient = ClientBuilder.newClient(new ClientConfig());
        locationCache = newBuilder().maximumSize(10000).concurrencyLevel(16).build(createLocationCacheLoader());
    }

    @Override
    public Location findLocation(@NotBlank String address) {
        return locationCache.getUnchecked(address);
    }

    private CacheLoader<String, Location> createLocationCacheLoader() {
        return new CacheLoader<String, Location>() {
            @Override
            public Location load(String address) throws Exception {
                return doFindLocation(address);
            }
        };
    }

    private Location doFindLocation(String address) {
        try {
            WebTarget target = createWebTarget(address);
            Response rs = target.request(APPLICATION_JSON).get();
            if (rs.hasEntity()) {
                String json = rs.readEntity(String.class);
                JSONObject jsonObject = new JSONObject(json);
                if (containsResults(jsonObject)) {
                    return getLocation(jsonObject);
                }
            }
            throw new LocationNotFoundException("Unknown location: " + address);
        } catch (Exception ex) {
            String err = "Failed to get location, reason " + ex.getMessage();
            log.error(err, ex);
            throw new LocationServiceException(err, ex);
        }
    }

    private WebTarget createWebTarget(String address) {
        return restClient.target(fromUri(apiRoot).path(apiPath + "/json")
                .queryParam(addressParam, address)
                .queryParam(keyParam, apiKey).build());
    }

    private boolean containsResults(JSONObject jsonObject) {
        String status = jsonObject.getString("status");
        if ("OK".equalsIgnoreCase(status)) {
            return true;
        } else if ("ZERO_RESULTS".equalsIgnoreCase(status)) {
            return false;
        } else {
            throw new LocationServiceException("Failed to get location, reason: " + status);
        }
    }

    private Location getLocation(JSONObject jsonObject) {
        JSONObject result = jsonObject.getJSONArray("results").getJSONObject(0);
        JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
        double lat = location.getDouble("lat");
        double lng = location.getDouble("lng");
        return new Location(lat, lng);
    }

    @Override
    public double calculateDistance(@NotNull Location first, @NotNull Location second) {
        return doCalculateDistance(first.getLat(), first.getLng(), second.getLat(), second.getLng());
    }

    private double doCalculateDistance(double lat1, double lng1, double lat2, double lng2) {
        int r = 6371;
        double latDistance = toRadians(lat2 - lat1), lngDistance = toRadians(lng2 - lng1);
        double a = sin(latDistance / 2) * sin(latDistance / 2);
        double b = cos(toRadians(lat1)) * cos(toRadians(lat2)) * sin(lngDistance / 2) * sin(lngDistance / 2);
        a += b;
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return r * c * 1000;
    }
}
