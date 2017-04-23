package com.recovery.netwrok.model;

import java.util.List;

/**
 * Created by tom on 2017/4/22.
 */

public class CollectRecordInfo {

    /**
     * vendorId : ls8d0s09D0Sklg
     * product : 土鸡蛋
     * unit : 个
     * latitude : 30.45398
     * longitude : 120.46398
     * position : 浙江临安天目山附件
     * tagId : kPDs87E2
     * unitIds : ["kgdlslkg","dl9ds0lg"]
     * images : ["http://chnagiot.com/a.jpg","http://chnagiot.com/b.jpg","http://chnagiot.com/c.jpg","http://chnagiot.com/d.jpg"]
     */

    private String vendorId;
    private String product;
    private String unit;
    private String latitude;
    private String longitude;
    private String position;
    private String tagId;
    private List<String> unitIds;
    private List<String> images;
    private Double quantity;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public List<String> getUnitIds() {
        return unitIds;
    }

    public void setUnitIds(List<String> unitIds) {
        this.unitIds = unitIds;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
