package com.wts.util;

public class Util {
    public static final String ADMIN = "whosyourdaddy";
    public static final String PWD = "hyrswts";

    public static String CheckNull(String str) {
        if (str==null){
            return "";
        }else{
            return str.trim();
        }
    }

}
