package com.github.hdesale;

import com.github.hdesale.model.Shop;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static com.github.hdesale.TestUtils.*;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

    @AfterClass
    public static void tearDown() {
        client.close();
    }

    @Test
    public void testAddShop() {
        // post a valid shop first
        Shop shop = createShop("Tesco", "15", "E14 4QT");
        Response rs = getShopsTarget(client).request().post(entity(shop, APPLICATION_JSON));
        assertNotNull(rs);
        assertCreated(rs);

        // post the same shop details again and expect a bad response
        rs = getShopsTarget(client).request().post(entity(shop, APPLICATION_JSON));
        assertNotNull(rs);
        assertBadRequest(rs);
    }

    @Test
    public void testGetShop() {
        // post a valid shop first
        Shop shop = createShop("ABC", "10", "E14 3AQ");
        Response rs = getShopsTarget(client).request().post(entity(shop, APPLICATION_JSON));
        assertNotNull(rs);
        assertCreated(rs);
        String link = rs.getHeaderString("location");
        assertNotNull(link);

        // get the same shop by valid id
        rs = client.target(link).request(APPLICATION_JSON).get();
        assertNotNull(rs);
        assertOk(rs);
        Shop shopFromServer = rs.readEntity(Shop.class);
        assertNotNull(shopFromServer);
        assertNotNull(shopFromServer.getShopLatitude());
        assertNotNull(shopFromServer.getShopLongitude());

        // try to get a shop by invalid id
        rs = client.target(link + "281347").request(APPLICATION_JSON).get();
        assertNotNull(rs);
        assertNotFound(rs);
    }

    @Test
    public void testRemoveShop() {
        // post a valid shop first
        Shop shop = createShop("XYZ", "5", "E14 5AH");
        Response rs = getShopsTarget(client).request().post(entity(shop, APPLICATION_JSON));
        assertNotNull(rs);
        assertCreated(rs);
        String link = rs.getHeaderString("location");
        assertNotNull(link);

        // delete the same shop by valid id
        rs = client.target(link).request().delete();
        assertNotNull(rs);
        assertNoContent(rs);

        // try to delete the same shop again
        rs = client.target(link).request().delete();
        assertNotNull(rs);
        assertNotFound(rs);
    }

    @Test
    public void testFindNearestShop() {
        // post first valid shop
        Shop shop = createShop("PQR", "15", "E14 5AH");
        Response rs = getShopsTarget(client).request().post(entity(shop, APPLICATION_JSON));
        assertNotNull(rs);
        assertCreated(rs);
        String link = rs.getHeaderString("location");
        assertNotNull(link);

        // post second valid shop
        shop = createShop("LMN", "280", "E14 3WF");
        rs = getShopsTarget(client).request().post(entity(shop, APPLICATION_JSON));
        assertNotNull(rs);
        assertCreated(rs);
        link = rs.getHeaderString("location");
        assertNotNull(link);

        // find the nearest shop (second shop should be near)
        rs = getShopsTarget(client).path("near-me")
                .queryParam("customerLatitude", 62.5064364)
                .queryParam("customerLongitude", 16.0288887)
                .request(APPLICATION_JSON)
                .get();
        assertNotNull(rs);
        assertOk(rs);
        Shop shopFromServer = rs.readEntity(Shop.class);
        assertNotNull(shopFromServer);
        assertTrue(shopFromServer.getShopName().equals("LMN"));
    }
}
