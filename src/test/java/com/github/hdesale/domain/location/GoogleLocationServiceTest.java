package com.github.hdesale.domain.location;

import com.github.hdesale.RetailManagerApplication;
import com.github.hdesale.model.Shop;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.github.hdesale.TestUtils.createShop;
import static com.github.hdesale.TestUtils.getShopAddressString;
import static org.junit.Assert.*;

/**
 * JUnit test for {@link GoogleLocationService} and other classes from the same package.
 *
 * @author Hemant
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailManagerApplication.class)
public class GoogleLocationServiceTest {

    @Autowired
    @Qualifier("googleLocationService")
    private LocationService locationService;

    @Test
    public void testFindLocation() throws Exception {
        Shop shop = createShop("Tesco", "15", "E14 4QT");
        Location location = locationService.findLocation(getShopAddressString(shop));
        assertNotNull(location);
        assertNotEquals(0, location.getLat(), 0);
        assertNotEquals(0, location.getLng(), 0);
    }

    @Test(expected = UncheckedExecutionException.class)
    public void testFindLocationWithBlankAddress() throws Exception {
        locationService.findLocation("");
    }

    @Test(expected = UncheckedExecutionException.class)
    public void testFindLocationWithInvalidAddress() throws Exception {
        locationService.findLocation("LKAjs;oaiej3rklja;kjA;O;;okjfoiwjurlkfjk");
    }

    @Test
    public void testCalculateDistance() throws Exception {
        Location first = new Location(1.0, 1.0);
        Location second = new Location(0, 0);
        assertNotEquals(first, second);

        // distance should be more than 0
        double distance = locationService.calculateDistance(first, second);
        assertTrue(distance > 0);

        // distance should be 0
        distance = locationService.calculateDistance(first, first);
        assertTrue(distance == 0);
    }
}