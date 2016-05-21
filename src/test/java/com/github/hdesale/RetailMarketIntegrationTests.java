package com.github.hdesale;

import com.github.hdesale.model.Shop;
import com.github.hdesale.model.ShopAddress;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Integration tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailManagerApplication.class)
@WebIntegrationTest("server.port:9000")
public class RetailMarketIntegrationTests {

    private static Client client;

    @BeforeClass
    public static void setUp() throws KeyManagementException, NoSuchAlgorithmException {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
                .nonPreemptive()
                .credentials("client", "abc12345")
                .build();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(feature);
        client = ClientBuilder.newClient(clientConfig);
    }

    private static Shop createShop(String name, String number, String postCode) {
        Shop shop = new Shop();
        shop.setShopName(name);
        ShopAddress shopAddress = new ShopAddress();
        shopAddress.setNumber(number);
        shopAddress.setPostCode(postCode);
        shop.setShopAddress(shopAddress);
        return shop;
    }

    @Test
    public void testPost() {
        // post a valid shop first
        Shop shop = createShop("Tesco", "15", "E14 4QT");
        Response rs = client.target("http://localhost:9000").path("v1/shops")
                .request()
                .post(Entity.entity(shop, MediaType.APPLICATION_JSON));
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.CREATED.getStatusCode());

        // post the same shop details again and expect a bad response
        rs = client.target("http://localhost:9000").path("v1/shops")
                .request()
                .post(Entity.entity(shop, MediaType.APPLICATION_JSON));
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testGetById() {
        // post a valid shop first
        Shop shop = createShop("ABC", "10", "E14 3AQ");
        Response rs = client.target("http://localhost:9000").path("v1/shops")
                .request()
                .post(Entity.entity(shop, MediaType.APPLICATION_JSON));
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.CREATED.getStatusCode());
        String link = rs.getHeaderString("location");
        Assert.assertNotNull(link);

        // get the same shop by valid id
        rs = client.target(link).request(MediaType.APPLICATION_JSON).get();
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.OK.getStatusCode());
        Shop shopFromServer = rs.readEntity(Shop.class);
        Assert.assertNotNull(shopFromServer);
        Assert.assertNotNull(shopFromServer.getShopLatitude());
        Assert.assertNotNull(shopFromServer.getShopLongitude());

        // try to get a shop by invalid id
        rs = client.target(link + "281347").request(MediaType.APPLICATION_JSON).get();
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testRemoveById() {
        // post a valid shop first
        Shop shop = createShop("XYZ", "5", "E14 5AH");
        Response rs = client.target("http://localhost:9000").path("v1/shops")
                .request()
                .post(Entity.entity(shop, MediaType.APPLICATION_JSON));
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.CREATED.getStatusCode());
        String link = rs.getHeaderString("location");
        Assert.assertNotNull(link);

        // delete the same shop by valid id
        rs = client.target(link).request().delete();
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.NO_CONTENT.getStatusCode());

        // try to delete the same shop again
        rs = client.target(link).request().delete();
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testFindNearestShop() {
        // post first valid shop
        Shop shop = createShop("PQR", "15", "E14 5AH");
        Response rs = client.target("http://localhost:9000").path("v1/shops")
                .request()
                .post(Entity.entity(shop, MediaType.APPLICATION_JSON));
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.CREATED.getStatusCode());
        String link = rs.getHeaderString("location");
        Assert.assertNotNull(link);

        // post second valid shop
        shop = createShop("LMN", "280", "E14 3WF");
        rs = client.target("http://localhost:9000").path("v1/shops")
                .request()
                .post(Entity.entity(shop, MediaType.APPLICATION_JSON));
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.CREATED.getStatusCode());
        link = rs.getHeaderString("location");
        Assert.assertNotNull(link);

        // find the nearest shop (second shop should be near)
        rs = client.target("http://localhost:9000").path("v1/shops/near-me")
                .queryParam("customerLatitude", 62.5064364)
                .queryParam("customerLongitude", 16.0288887)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertNotNull(rs);
        Assert.assertTrue(rs.getStatus() == Response.Status.OK.getStatusCode());
        Shop shopFromServer = rs.readEntity(Shop.class);
        Assert.assertNotNull(shopFromServer);
        Assert.assertTrue(shopFromServer.getShopName().equals("LMN"));
    }
}
