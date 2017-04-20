package com.recovery.scene.model;

import java.io.Serializable;

/**
 * Created by tom on 2017/4/20.
 */

public class ProductItemPOJO implements Serializable{
    private long time;
    private String productName;
    private String code;
    private String owner;
    private String state;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
