package com.family_financial_system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family_financial_system.bean.message;
import com.family_financial_system.service.userService;
import com.family_financial_system.util.JedisPoolUtils;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/chatMessage")
public class userCharController {
    @Autowired
    userService userService;
    //获取历史聊天记录
    @RequestMapping("/getHistoryChat")
    @ResponseBody
    public String getHistoryChar(String sender,String receiver){
        JSONArray jsonArray =null;
        Jedis jedis= JedisPoolUtils.getJedis();
        List<message> messageList=userService.getChatHistory(sender,receiver);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class,new com.family_financial_system.util.JsonDateValueProcessor());
        if(messageList!=null){
            jsonArray=JSONArray.fromObject(messageList);
        }else{


            jsonArray= JSONArray.fromObject(jedis.get(sender+"."+receiver+"message"));

            if(jedis.get("temporalMessageData")!=null){
                org.json.JSONArray jsonArray1=new org.json.JSONArray(jedis.get("temporalMessageData"));
            }
        }
        System.out.println(jsonArray);
        return jsonArray.toString();
    }
    //存储历史聊天记录
    @RequestMapping("/inputHistoryChat")
    @ResponseBody
    public String inputHistoryChat() throws ParseException {
        Jedis jedis= JedisPoolUtils.getJedis();
        List<message> messages=new ArrayList<message>();
        JSONObject jsonObject=new JSONObject();
        if(!Objects.equals(jedis.get("temporalMessageData"), "[]")&&!Objects.equals(jedis.get("temporalMessageData"),null)){
            org.json.JSONArray jsonArray=new org.json.JSONArray(jedis.get("temporalMessageData"));
            for(int i=0;i<jsonArray.length();i++){
                messages.add(new message(null,jsonArray.getJSONObject(i).get("message").toString(),
                        Integer.valueOf((String) jsonArray.getJSONObject(i).get("Sender")),
                        Integer.valueOf((String) jsonArray.getJSONObject(i).get("Receiver")),
                        new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) jsonArray.getJSONObject(i).get("MsgTime")).getTime())));
            }
//            userService.inputMessageData(messages);
            jsonObject.put("mess","数据已更新");
        }else{
            jsonObject.put("mess","无数据更新");
        }
        jedis.del("temporalMessageData");
        return jsonObject.toJSONString();

    }
    //获取用户在线状态
    @RequestMapping("/getUserState")
    @ResponseBody
    public String getUserState(){
        Jedis jedis= JedisPoolUtils.getJedis();
        HashMap<Integer,String> hashTempMap=new HashMap<>();

        try{
            Thread.sleep(1000);
            hashTempMap= JSON.parseObject(jedis.get("userState"),HashMap.class);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        org.json.JSONObject jsonObject=new org.json.JSONObject(hashTempMap);
        System.out.println("response........");
        return jsonObject.toString();
    }
}
