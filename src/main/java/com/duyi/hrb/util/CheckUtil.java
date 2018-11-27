package com.duyi.hrb.util;

public class CheckUtil {

    public static boolean checkNotNull(String... paramList) {
        for (int i = 0 ; i < paramList.length ; i ++){
            if (paramList[i] == null || "".equals(paramList[i])){
                return false;
            }
        }
        return true;
    }

    public static boolean checkFormat(String regex, String... paramList) {
        return true;
    }

}
