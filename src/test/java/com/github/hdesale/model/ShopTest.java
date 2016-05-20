package com.github.hdesale.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * JUnit test for {@link Shop} and {@link ShopAddress}
 *
 * @author Hemant
 */
public class ShopTest {

    private Shop shop;

    @Before
    public void setUp() {
        shop = new Shop();
        shop.setShopName("Test Shop");
        ShopAddress shopAddress = new ShopAddress();
        shopAddress.setNumber("12A");
        shopAddress.setPostCode("ABC DEF");
        shop.setShopAddress(shopAddress);
        shop.setShopLatitude(98.234);
        shop.setShopLongitude(-402.3234);
    }

    @Test
    public void testShopName() throws Exception {
        assertNotNull(shop.getShopName());
        assertEquals("Test Shop", shop.getShopName());
        assertNotEquals(" Test Shop", shop.getShopName());
    }

    @Test
    public void testShopAddress() throws Exception {
        assertNotNull(shop.getShopAddress());
        assertNotNull(shop.getShopAddress().getNumber());
        assertEquals("12A", shop.getShopAddress().getNumber());
        assertNotEquals("12a", shop.getShopAddress().getNumber());
        assertNotNull(shop.getShopAddress().getPostCode());
        assertEquals("ABC DEF", shop.getShopAddress().getPostCode());
        assertNotEquals("ABCDEF", shop.getShopAddress().getPostCode());
    }

    @Test
    public void testShopLongitude() throws Exception {
        assertEquals(98.234, shop.getShopLatitude(), 0);
        assertNotEquals(98.233, shop.getShopLatitude(), 0);
    }

    @Test
    public void testShopLatitude() throws Exception {
        assertEquals(-402.3234, shop.getShopLongitude(), 0);
        assertNotEquals(98.233, shop.getShopLongitude(), 0);
    }

    @Test
    public void testShopAddressString() throws Exception {
        assertNotNull(shop.getShopAddress().buildAddressString());
        String str1 = shop.getShopAddress().buildAddressString();
        String str2 = shop.getShopAddress().buildAddressString();
        assertEquals(str1, str2);
        assertEquals("12A ABC DEF", str1);
    }
}