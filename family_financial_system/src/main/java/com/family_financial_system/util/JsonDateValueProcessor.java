package com.family_financial_system.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author 江冷易水寒
 * @data 2020/6/19 13:12
 */

public class JsonDateValueProcessor implements JsonValueProcessor {

    private String format = "yyyy-MM-dd";

    @Override
    public Object processArrayValue(Object value, JsonConfig config) {
        return process(value);
    }

    @Override
    public Object processObjectValue(String arg0, Object value, JsonConfig config) {
        return process(value);
    }

    private Object process(Object value){

        //遇到类型为日期的，就自动转换成“yyyy-MM-dd HH:mm:ss”格式的字符串
        if(value instanceof Date){
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);
            return sdf.format(value);
        }
        return value == null ? "" : value.toString();
    }
}