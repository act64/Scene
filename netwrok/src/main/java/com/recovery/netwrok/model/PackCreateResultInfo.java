package com.recovery.netwrok.model;

/**
 * Created by tom on 2017/4/22.
 */

public class PackCreateResultInfo {

    /**
     * id : lsig98alkgasdf
     * createdAt : 2017-05-14 12:24:39
     * state : 0
     * vendor : 小张
     * product : 土鸡蛋
     */

    private String id;
    private String createdAt;
    private String state;
    private String vendor;
    private String product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
