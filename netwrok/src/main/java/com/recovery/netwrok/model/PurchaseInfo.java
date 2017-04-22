package com.recovery.netwrok.model;

import java.util.List;

/**
 * Created by tom on 2017/4/22.
 */

public class PurchaseInfo {

    /**
     * total : 23
     * totalPage : 3
     * currentPage : 1
     * list : [{"vendor":"小张","time":"2017-03-11 14:25:36","product":"土鸡蛋","quantity":56,"unit":"斤"},{"vendor":"小张","time":"2017-03-11 14:25:36","product":"土鸡蛋","quantity":56,"unit":"斤"}]
     */

    private int total;
    private int totalPage;
    private int currentPage;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * vendor : 小张
         * time : 2017-03-11 14:25:36
         * product : 土鸡蛋
         * quantity : 56
         * unit : 斤
         */

        private String vendor;
        private String time;
        private String product;
        private int quantity;
        private String unit;

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
