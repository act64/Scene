package com.recovery.netwrok.exception;

/**
 * Created by tom on 2017/4/22.
 */

public class NetException extends RuntimeException {
    public static final int NoNetWork=-2001;
    public static final int NetWorkOk=0;
    private int code;
    public NetException(int errorcode,String msg){
        super(msg);
        this.code=errorcode;
    }

    public int getCode() {
        return code;
    }
}
