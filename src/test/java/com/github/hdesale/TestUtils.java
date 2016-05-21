package com.github.hdesale;

import com.github.hdesale.model.Shop;
import com.github.hdesale.model.ShopAddress;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Objects;

import static javax.ws.rs.core.Response.Status.*;
import static org.junit.Assert.assertTrue;

/**
 * Test utility class.
 *
 * @author Hemant
 */
public class TestUtils {

    private TestUtils() {
        // private constructor to enforce static utilisation
    }

    public static Shop createShop(String shopName, String number, String postCode) {
        Shop shop = new Shop();
        shop.setShopName(shopName);
        ShopAddress shopAddress = new ShopAddress();
        shopAddress.setNumber(number);
        shopAddress.setPostCode(postCode);
        shop.setShopAddress(shopAddress);
        return shop;
    }

    public static String getShopAddressString(Shop shop) {
        return shop.getShopAddress().buildAddressString();
    }

    public static boolean deepEquals(Shop shop1, Shop shop2) {
        return Objects.equals(shop1.getShopName(), shop2.getShopName()) &&
                deepEquals(shop1.getShopAddress(), shop2.getShopAddress());
    }

    public static boolean deepEquals(ShopAddress shopAddress1, ShopAddress shopAddress2) {
        return Objects.equals(shopAddress1.getNumber(), shopAddress2.getNumber()) &&
                Objects.equals(shopAddress1.getPostCode(), shopAddress2.getPostCode());
    }

    public static WebTarget getShopsTarget(Client client) {
        return client.target("http://localhost:9000").path("v1/shops");
    }

    public static void assertOk(Response rs) {
        assertTrue(rs.getStatus() == OK.getStatusCode());
    }

    public static void assertCreated(Response rs) {
        assertTrue(rs.getStatus() == CREATED.getStatusCode());
    }

    public static void assertNoContent(Response rs) {
        assertTrue(rs.getStatus() == NO_CONTENT.getStatusCode());
    }

    public static void assertNotFound(Response rs) {
        assertTrue(rs.getStatus() == NOT_FOUND.getStatusCode());
    }

    public static void assertBadRequest(Response rs) {
        assertTrue(rs.getStatus() == BAD_REQUEST.getStatusCode());
    }
}
