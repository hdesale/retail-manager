package com.github.hdesale.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Model class for a shop.
 *
 * @author Hemant
 */
public class Shop {

    @NotBlank(message = "Shop: 'name' is mandatory")
    private String shopName;

    @Valid
    @NotNull(message = "Shop: 'shopAddress' is mandatory")
    private ShopAddress shopAddress;

    private double shopLongitude;

    private double shopLatitude;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public ShopAddress getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(ShopAddress shopAddress) {
        this.shopAddress = shopAddress;
    }

    public double getShopLongitude() {
        return shopLongitude;
    }

    public void setShopLongitude(double shopLongitude) {
        this.shopLongitude = shopLongitude;
    }

    public double getShopLatitude() {
        return shopLatitude;
    }

    public void setShopLatitude(double shopLatitude) {
        this.shopLatitude = shopLatitude;
    }
}
