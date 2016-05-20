package com.github.hdesale.api;

import com.github.hdesale.model.Shop;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

/**
 * JAX-RS resource interface for shops.
 *
 * @author Hemant
 */
public interface ShopResource {

    /**
     * Adds shop into repository if it does not already exist.
     *
     * @param shop to be added into repository
     * @return JAX-RS response
     */
    Response addShop(@Valid Shop shop);

    /**
     * Gets the shop by id.
     *
     * @param id unique id of a shop
     * @return JAX-RS response
     */
    Response getShop(@NotBlank String id);

    /**
     * Removes shop with matching id from repository.
     *
     * @param id unique id of a shop
     * @return JAX-RS response
     */
    Response removeShop(@NotBlank String id);

    /**
     * Finds a nearest shop to customer.
     *
     * @param customerLongitude longitude value
     * @param customerLatitude latitude value
     * @return JAX-RS response
     */
    Response findNearestShop(@NotNull Double customerLongitude, @NotNull Double customerLatitude);
}
