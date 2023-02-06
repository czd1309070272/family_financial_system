package com.family_financial_system.util;


import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang.time.DateUtils;


public class parseDate {
   private static String[] parsePatterns={"mm:ss","MM-dd mm:ss","mm：ss","MM-dd mm：ss","MM-ddmm:ss","MM-ddmm：ss"};
   public Date parseDate(String string){
       if(string==null){
           return null;
       }
       try {
           return DateUtils.parseDate(string,parsePatterns);
       } catch (ParseException e) {
           return null;
       }
   }
}
