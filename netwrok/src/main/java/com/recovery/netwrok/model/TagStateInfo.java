package com.recovery.netwrok.model;

import com.google.gson.annotations.Expose;

/**
 * Created by tom on 2017/4/22.
 */

public class TagStateInfo {


    /**
     * state : 1
     */

    private int state;
   @Expose(serialize = false,deserialize = false)
    private String codeId;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        switch (state){
            case 0:return "未使用";
            case 1:return "已使用";
            case 2:return "已作废";
        }
        return "未知标签";
    }
}
