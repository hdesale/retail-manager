package com.github.hdesale.domain.shop;

import com.github.hdesale.model.Shop;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory repository implementation of {@link ShopRepository}.
 *
 * @author Hemant
 */
@Service("inMemoryShopRepository")
public class InMemoryShopRepository implements ShopRepository {

    private ConcurrentHashMap<String, Shop> shops;

    @PostConstruct
    public void init() {
        shops = new ConcurrentHashMap<>();
    }

    @Override
    public String addShop(Shop shop) {
        String id = generateId(shop);
        if (shops.putIfAbsent(id, shop) == null) {
            return id;
        }
        throw new IllegalArgumentException("Shop was already added in repository, id: " + id);
    }

    private String generateId(Shop shop) {
        String s = shop.getShopName() + shop.getShopAddress().getNumber() + shop.getShopAddress().getPostCode();
        return s.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    @Override
    public Optional<Shop> getShop(String id) {
        return Optional.ofNullable(shops.get(id));
    }

    @Override
    public Collection<Shop> getAllShops() {
        return Collections.unmodifiableCollection(shops.values());
    }

    @Override
    public boolean removeShop(String id) {
        return shops.remove(id) != null;
    }
}
