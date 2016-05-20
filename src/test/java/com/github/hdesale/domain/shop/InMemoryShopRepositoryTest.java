package com.github.hdesale.domain.shop;

import com.github.hdesale.model.Shop;
import com.github.hdesale.model.ShopAddress;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

/**
 * JUnit test for {@link InMemoryShopRepositoryTest}
 *
 * @author Hemant
 */
public class InMemoryShopRepositoryTest {

    private static InMemoryShopRepository repository;

    private static String id;

    private static Shop shop;

    @BeforeClass
    public static void setUp() {
        repository = new InMemoryShopRepository();
        repository.init();
        id = repository.addShop(createShop("12A"));
    }

    private static Shop createShop(String number) {
        shop = new Shop();
        shop.setShopName("Test Shop");
        ShopAddress shopAddress = new ShopAddress();
        shopAddress.setNumber(number);
        shopAddress.setPostCode("ABC DEF");
        shop.setShopAddress(shopAddress);
        shop.setShopLatitude(98.234);
        shop.setShopLongitude(-402.3234);
        return shop;
    }

    @Test
    public void testAddShop() throws Exception {
        assertNotNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateAddShop() throws Exception {
        assertNotNull(id);
        repository.addShop(createShop("12A"));
    }

    @Test
    public void testGetShop() throws Exception {
        assertNotNull(repository.getShop(id));
        Shop shop1 = repository.getShop(id).orElse(null);
        Shop shop2 = repository.getShop(id).orElse(null);
        assertTrue(shop1 == shop2);
        Objects.deepEquals(shop, repository.getShop(id));
        assertFalse(repository.getShop("23942039KNKJHLIJKJOD").isPresent());
    }

    @Test
    public void testGetAllShops() throws Exception {
        repository.addShop(createShop("11A"));
        repository.addShop(createShop("13B"));
        assertEquals(3, repository.getAllShops().size());
    }

    @Test
    public void testRemoveShop() throws Exception {
        int sizeBefore = repository.getAllShops().size();
        repository.removeShop(id);
        int sizeAfter = repository.getAllShops().size();
        assertEquals(sizeAfter, sizeBefore - 1);
    }
}