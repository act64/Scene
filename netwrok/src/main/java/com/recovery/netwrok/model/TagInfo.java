package com.recovery.netwrok.model;

import com.google.gson.annotations.Expose;

/**
 * Created by tom on 2017/4/22.
 */

public class TagInfo {

    /**
     * id : lsig98alkgasdf
     * createdAt : 2017-05-14 12:24:39
     * state : 0
     * vendor : 小张
     * product : 土鸡蛋
     */

    private String id;
    private String createdAt;
    private int state;
    private String vendor;
    private String product;

    public String getCodeID() {
        return codeID;
    }

    public void setCodeID(String codeID) {
        this.codeID = codeID;
    }

    @Expose(serialize = false,deserialize = false)
    private String codeID;

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
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

    public String getStateStr(){
        if (state==0) return "正常";
        if (state==1) return "已销码";
        return "";

    }
}
