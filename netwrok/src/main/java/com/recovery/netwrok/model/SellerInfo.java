package com.recovery.netwrok.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tom on 2017/4/22.
 */

public class SellerInfo implements Serializable {

    /**
     * id : 838dls0sjgla0
     * name : 王小毛
     * sex : 男
     * regDate : 2017-02-23
     * regAddr : 浙江省临安市天目山
     * mainBiz : 土鸡蛋，农家菜
     * lastTrade : 2017-05-27
     * warn : 注册地址与当前地址有过大的距离，请注意辨别真伪
     * images : ["http://www.chnagiot.com/a.jpg","http://www.chnagiot.com/b.jpg","http://www.chnagiot.com/c.jpg","http://www.chnagiot.com/d.jpg"]
     */

    private String id;
    private String name;
    private String sex;
    private String regDate;
    private String regAddr;
    private String mainBiz;
    private String lastTrade;
    private String warn;
    private List<String> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegAddr() {
        return regAddr;
    }

    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr;
    }

    public String getMainBiz() {
        return mainBiz;
    }

    public void setMainBiz(String mainBiz) {
        this.mainBiz = mainBiz;
    }

    public String getLastTrade() {
        return lastTrade;
    }

    public void setLastTrade(String lastTrade) {
        this.lastTrade = lastTrade;
    }

    public String getWarn() {
        return warn;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
