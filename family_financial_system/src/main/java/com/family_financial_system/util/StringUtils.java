package com.family_financial_system.util;

public class StringUtils {
    public static Boolean notEmpty(String... str) {
        for (String strIndex : str) {
            if (StringUtils.isEmpty(strIndex)) {
                return false;
            }
        }
        return true;
    }

    public static Boolean isEmpty(String... str) {
        return !notEmpty(str);
    }
}
