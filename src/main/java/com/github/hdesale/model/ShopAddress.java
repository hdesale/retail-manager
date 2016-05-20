package com.github.hdesale.model;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Model class for shop address.
 *
 * @author Hemant
 */
public class ShopAddress {

    @NotBlank(message = "Shop address: 'number' is mandatory")
    private String number;

    @NotBlank(message = "Shop address: 'postCode' is mandatory")
    private String postCode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String buildAddressString() {
        return (getNumber() + " " + getPostCode()).replaceAll("\\s", " ").toUpperCase();
    }
}
