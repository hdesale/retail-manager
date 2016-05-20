package com.github.hdesale.domain.location;

import com.github.hdesale.RetailManagerApplication;
import com.github.hdesale.model.Shop;
import com.github.hdesale.model.ShopAddress;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    private static Shop shop;

    private static String shopAddressString;

    @BeforeClass
    public static void setUp() {
        shop = createShop("15");
    }

    private static Shop createShop(String number) {
        shop = new Shop();
        shop.setShopName("Tesco");
        ShopAddress shopAddress = new ShopAddress();
        shopAddress.setNumber(number);
        shopAddress.setPostCode("E14 4QT");
        shop.setShopAddress(shopAddress);
        shopAddressString = shop.getShopAddress().buildAddressString();
        return shop;
    }

    @Test
    public void testFindLocation() throws Exception {
        Location location = locationService.findLocation(shopAddressString);
        assertNotNull(location);
        assertNotEquals(0, location.getLat(), 0);
        assertNotEquals(0, location.getLng(), 0);
    }

    @Test(expected = UncheckedExecutionException.class)
    public void testFindWrongLocation() throws Exception {
        String address = "LKAjs;oaiej3rklja;kjA;O;;okjfoiwjurlkfjk";
        locationService.findLocation(address);
    }

    @Test
    public void testCalculateDistance() throws Exception {
        Location first = new Location(1.0, 1.0);
        Location second = new Location(0, 0);
        assertNotEquals(first, second);
        double distance = locationService.calculateDistance(first, second);
        assertTrue(distance > 0);
    }
}