package com.recovery.netwrok.model;

import java.util.List;

/**
 * Created by tom on 2017/4/22.
 */

public class PackageCreateInfo {

    /**
     * packageCode : 98LLGJDO
     * productList : ["jgkdls","jgd998sl","kdlsglal"]
     */

    private String packageCode;
    private List<String> productList;

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }
}
