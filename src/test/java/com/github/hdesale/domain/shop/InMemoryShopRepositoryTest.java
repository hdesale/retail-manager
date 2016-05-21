package com.github.hdesale.domain.shop;

import com.github.hdesale.RetailManagerApplication;
import com.github.hdesale.model.Shop;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.github.hdesale.TestUtils.createShop;
import static com.github.hdesale.TestUtils.deepEquals;
import static com.github.hdesale.TestUtils.getShopAddressString;
import static org.junit.Assert.*;

/**
 * JUnit test for {@link InMemoryShopRepositoryTest}
 *
 * @author Hemant
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailManagerApplication.class)
public class InMemoryShopRepositoryTest {

    @Autowired
    @Qualifier("inMemoryShopRepository")
    private ShopRepository repository;

    private String id;

    private Shop shop;

    @Before
    public void setUp() {
        shop = createShop("Test Shop", "12A", "ABC DEF");
        id = repository.addShop(shop);
    }

    @After
    public void tearDown() {
        repository.removeShop(id);
    }

    @Test
    public void testAddShop() throws Exception {
        assertNotNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateAddShop() throws Exception {
        Shop shop = createShop("Test Shop", "12A", "ABC DEF");
        repository.addShop(shop);
    }

    @Test
    public void testGetShop() throws Exception {
        assertNotNull(repository.getShop(id));

        Shop shop1 = repository.getShop(id).orElse(null);
        Shop shop2 = repository.getShop(id).orElse(null);
        assertTrue(deepEquals(shop, shop1));
        assertTrue(deepEquals(shop1, shop2));

        Shop notAddedShop = createShop("Not added", "1A", "IRY OIO");
        assertFalse(repository.getShop(getShopAddressString(notAddedShop)).isPresent());
    }

    @Test
    public void testGetAllShops() throws Exception {
        assertNotNull(repository.getShop(id));
        repository.addShop(createShop("Second", "1A", "IRY AIO"));
        repository.addShop(createShop("Third", "2A", "IRY BIO"));
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