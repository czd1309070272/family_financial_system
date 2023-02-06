package com.family_financial_system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family_financial_system.bean.bill;
import com.family_financial_system.bean.user;
import com.family_financial_system.service.userService;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class dataReceiveController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    userService userService;
    @RequestMapping(value = "addbillinfo",method = RequestMethod.POST)
    @ResponseBody
    void addbillInfo(String goodsName,String selectType,String selectWay,String goodsPrice,String defaultDateTime,String defaultTime,String remark){
        HttpSession httpSession = httpServletRequest.getSession();
        Object userobject=httpSession.getAttribute("user");
        String dateTime=defaultDateTime+" "+defaultTime;
        user user=(user) userobject;
        Timestamp ts = Timestamp.valueOf(dateTime);
        if(remark==null|| remark.equals("")){
            remark=null;
        }
        if(userService.addBill(goodsName,user.getId(),"￥",Double.valueOf(goodsPrice),Integer.valueOf(selectType),remark,Integer.valueOf(selectWay),ts)){
            System.out.println("添加成功");
        }
    }
    @RequestMapping(value = "delbill",method = RequestMethod.POST)
    @ResponseBody
    String delBill(@RequestParam("billIndex[]") List<String> billIndex){
        JSONObject jsonObject=new JSONObject();
        if(billIndex!=null){
            jsonObject.put("msg","已删除"+userService.delBill(billIndex,true)+"条数据");
        }else {
            jsonObject.put("msg","无数据删除");
        }
        return JSONObject.toJSONString(jsonObject);
    }
    @RequestMapping(value = "updatebill")
    @ResponseBody
    String updatebill(String GoodsID,String GoodsName,String GoodsType,String remark,String GoodsPayType,String GoodsMoney,String nowDateTime){
        JSONObject jsonObject=new JSONObject();
        try{
            userService.updatebill(!Objects.equals(GoodsName, "null") ?GoodsName:null,
                    !Objects.equals(GoodsMoney, "null") ?Double.valueOf(GoodsMoney):null,
                    !Objects.equals(GoodsType, "null") ?Integer.valueOf(GoodsType):null,
                    !Objects.equals(remark, "null") ?remark:null,
                    !Objects.equals(GoodsPayType, "null") ?Integer.valueOf(GoodsPayType):null,
                    !Objects.equals(nowDateTime, "null") ?nowDateTime:null,Integer.valueOf(GoodsID));
        } catch (ParseException e) {

            throw new RuntimeException(e);
        }
        jsonObject.put("msg","数据更新失败");
        return JSONObject.toJSONString(jsonObject);
    }
    @RequestMapping("/flotData")
    @ResponseBody
    public String flotData(){
        JSONObject jsonObject=new JSONObject();
        String houseid="";
        Cookie[] cookies=httpServletRequest.getCookies();
        for(int i=0;i< cookies.length;i++){
            Cookie cookie=cookies[i];
            if(cookie.getName().equals("cookieHouseID")){
                houseid=cookie.getValue();
            }
        }
        List<bill> billList=new ArrayList<>();
        if(getCookieUserid("role").equals("超级管理员")){
            billList=userService.getBill(Integer.valueOf(houseid),null);
        }else{
            billList=userService.getBill(Integer.valueOf(houseid),Integer.valueOf(getCookieUserid("userID")));
        }
        List<BigDecimal> Money=new ArrayList<BigDecimal>();
        for(int i=0;i<billList.size();i++){
            Money.add(billList.get(i).getMoney());
        }
        if(billList!=null){
            return JSON.parse(JSONObject.toJSONStringWithDateFormat(billList,"yyyy-MM-dd HH:mm:ss")).toString();
        }
        jsonObject.put("msg","数据更新失败");
        return JSONObject.toJSONString(jsonObject);
    }
    public String getCookieUserid(String name){
        String userid=null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for(Cookie c :cookies ){
            if(c.getName().equals(name)){
                userid= c.getValue();
            }

        }
        return userid;
    }


}
