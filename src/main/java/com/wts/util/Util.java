package com.wts.util;

public class Util {
    public static String CheckNull(String str) {
        if (str==null){
            return "";
        }else{
            return str.trim();
        }
    }

}
