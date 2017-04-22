package com.recovery.netwrok.model;

import java.io.Serializable;

/**
 * Created by tom on 2017/4/22.
 */

public class UserInfo implements Serializable {

    /**
     * id : 2hjkuuisjwid
     * name : 小王
     * sex : 男
     */

    private String id;
    private String name;
    private String sex;

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
}
