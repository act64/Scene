package com.recovery.scene.utils;

/**
 * Created by tom on 2017/4/22.
 */

public class APPUtils {
    public static String getTagCode(String code){
        if (code==null||!code.toLowerCase().trim().contains("http://qs0.me/v/")){
            return null;
        }
        String[] pits = code.split("/");
        if (pits!=null &&pits.length>0) {
            String pit = pits[pits.length - 1];
            if (pit.length()==8) {
                return pit;
            }
            return null;
        }
        return null;
    }
}
