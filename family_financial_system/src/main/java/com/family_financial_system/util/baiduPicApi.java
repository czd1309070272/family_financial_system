package com.family_financial_system.util;

import com.baidu.aip.ocr.AipOcr;
import com.family_financial_system.bean.bill;
import com.family_financial_system.service.pic2JsonService;
import com.family_financial_system.service.userService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class baiduPicApi {
    public static final String APP_ID = "28068422";
    public static final String API_KEY = "SIBjehEscIjOzv9qNIhuaGR5";
    public static final String SECRET_KEY = "3G6PtLIFwK6Ha5VGOw5Kb7g04Apt8tNQ";


    public List<bill> pic2ApiJson(byte[] pic, List<String> typeList){
        long startTime=System.currentTimeMillis();

        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("probability", "true");
        JSONObject res = client.accurateGeneral(pic, options);
        JSONArray result = res.getJSONArray("words_result");
//        System.out.println(result.toString(2));
        List<Integer> indexList=new ArrayList<>();


        for(int i=6;i<result.length();i++){
            for (int j=0;j<typeList.size();j++){
                if((result.getJSONObject(i).get("words").toString()).equals(typeList.get(j))){
                    indexList.add(i);
                }
            }
        }

        parseDate parseDate=new parseDate();


        String reg="[^0-9]+(.[^0-9])+[^0-9]";
        String testReg="-?[0-9]+.?[0-9]*";
        String title = null;
        String money=null;
        String type=null;
        String time=null;
        String remark=null;
        List<bill> billList=new ArrayList<>();
        Integer id=0;
        if(Double.valueOf(result.getJSONObject(indexList.get(0)-2).getJSONObject("probability").get("min").toString())<0.9||
                Double.valueOf(result.getJSONObject(indexList.get(0)-1).getJSONObject("probability").get("min").toString())<0.9||
                Double.valueOf(result.getJSONObject(indexList.get(0)).getJSONObject("probability").get("min").toString())<0.9){
            indexList.remove(0);
        }
        for(int i=0;i<indexList.size();i++){
            title=result.getJSONObject(indexList.get(i)-2).get("words").toString();
            if(parseDate.parseDate(result.getJSONObject(indexList.get(i)+1).get("words").toString().substring(2))==null){
                time=result.getJSONObject(indexList.get(i)+2).get("words").toString();
                remark=result.getJSONObject(indexList.get(i)+1).get("words").toString();
            }else{
                time=result.getJSONObject(indexList.get(i)+1).get("words").toString();
                remark="";
            }
            money=result.getJSONObject(indexList.get(i)-1).get("words").toString();
            type=result.getJSONObject(indexList.get(i)).get("words").toString();
            if(parseDate.parseDate(time.substring(2))!=null){
                if(!money.matches(testReg)) {
                    String select = result.getJSONObject(indexList.get(i) - 1).get("words").toString().replace("…","");
                    Pattern p = Pattern.compile(reg);
                    Matcher m = p.matcher(select.substring(select.length()/2));
                    money = select.charAt(select.indexOf(m.replaceAll("").trim().substring(0, 1)) - 1) + m.replaceAll("").trim();
                    title = select.substring(0, select.indexOf(m.replaceAll("").trim().substring(0, 1)) - 1);
                }
//                System.out.println(type + " " + title + " " + money + " " + timeTranform(time)+" "+remark);
                if(money.charAt(0)=='-'){
                    billList.add(new bill(id++,title,"小曾",type,"￥",new BigDecimal(money.replace("-","")),"支付宝",timeTranform(time),remark,1));
                }



            }
        }
        long endTime=System.currentTimeMillis();
        System.out.println("运行时间:"+(endTime-startTime)+"ms");
        return billList;
    }
    public Timestamp timeTranform(String time){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String dateNow=null;
        StringBuffer stringBuffer=new StringBuffer();
        switch (time.substring(0,2)){
            case "今天":dateNow=simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));break;
            case "昨天":dateNow=String.valueOf(cal.get(Calendar.YEAR))+"-"+String.valueOf(cal.get(Calendar.MONTH)+1)+"-"+String.valueOf(cal.get(Calendar.DATE)-1);break;
            default:stringBuffer.append(time.substring(0,5)+" ");stringBuffer.append(time.substring(5));dateNow=stringBuffer.toString();break;
        }
        if(time.substring(0,2).equals("今天")||time.substring(0,2).equals("昨天")){
            return Timestamp.valueOf(dateNow+" "+time.substring(2).replace("：",":")+":"+"00");
        }
        return Timestamp.valueOf((cal.get(Calendar.YEAR)+"-").concat(dateNow)+":"+"00");

    }
}
