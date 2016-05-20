package com.github.hdesale.domain.shop;

import com.github.hdesale.model.Shop;

import java.util.Collection;
import java.util.Optional;

/**
 * Repository for storing and retrieving shops data.
 *
 * @author Hemant
 * @see InMemoryShopRepository
 */
public interface ShopRepository {

    /**
     * Adds shop into repository if it does not exist.
     *
     * @param shop to be added to repository
     * @return unique id string generated for the shop
     */
    String addShop(Shop shop);

    /**
     * Gets a shop by id.
     *
     * @param id unique id of shop
     * @return an optional of shop
     */
    Optional<Shop> getShop(String id);

    /**
     * Gets all shops from repository.
     * <p>
     * The returned collection is an unmodifiable view of shops from repository. The
     * iterators created from this collection are supposed to be used by single thread
     * only. Also they will not throw {@link java.util.ConcurrentModificationException}
     * and are weakly-consistent which means they may or may not reflect any concurrent
     * modifications happening through add/remove by other threads.
     *
     * @return an unmodifiable view of all shops from repository
     */
    Collection<Shop> getAllShops();

    /**
     * Removes the shop of matching id from repository.
     *
     * @param id unique identifier of shop
     * @return true only shop was removed from repository
     */
    boolean removeShop(String id);
}
