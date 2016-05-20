package com.github.hdesale.api;

import com.github.hdesale.domain.location.Location;
import com.github.hdesale.domain.location.LocationService;
import com.github.hdesale.domain.shop.ShopRepository;
import com.github.hdesale.model.Shop;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

/**
 * JAX-RS resource implementation of {@link ShopResource}.
 *
 * @author Hemant
 */
@Component
@Path("/v1/shops")
public class ShopResourceImplV1 implements ShopResource {

    @Context
    private UriInfo uriInfo;

    @Autowired
    @Qualifier("googleLocationService")
    private LocationService locationService;

    @Autowired
    @Qualifier("inMemoryShopRepository")
    private ShopRepository shopRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response addShop(@Valid Shop shop) {
        Location location = locationService.findLocation(shop.getShopAddress().buildAddressString());
        updateLocation(shop, location);
        String id = shopRepository.addShop(shop);
        URI createdUri = uriInfo.getAbsolutePathBuilder().path(id).build();
        return Response.created(createdUri).build();
    }

    private void updateLocation(Shop shop, Location location) {
        shop.setShopLongitude(location.getLng());
        shop.setShopLatitude(location.getLat());
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getShop(@NotBlank @PathParam("id") String id) {
        Optional<Shop> shop = shopRepository.getShop(id);
        if (shop.isPresent()) {
            return Response.ok(shop.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Override
    public Response removeShop(@NotBlank @PathParam("id") String id) {
        if (shopRepository.removeShop(id)) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/near-me")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findNearestShop(@NotNull @QueryParam("customerLongitude") Double customerLongitude,
                                    @NotNull @QueryParam("customerLatitude") Double customerLatitude) {
        Location customerLocation = new Location(customerLatitude, customerLongitude);
        Collection<Shop> shops = shopRepository.getAllShops();
        Optional<Shop> optional = shops
                .stream()
                .min(Comparator.comparing(shop -> {
                    Location shopLocation = new Location(shop.getShopLatitude(), shop.getShopLongitude());
                    return locationService.calculateDistance(shopLocation, customerLocation);
                }));
        return (optional.isPresent() ? Response.ok(optional.get()) : Response.ok()).build();
    }
}
