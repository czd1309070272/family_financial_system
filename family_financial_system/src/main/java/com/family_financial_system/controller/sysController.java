package com.family_financial_system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family_financial_system.bean.bill;
import com.family_financial_system.bean.user;
import com.family_financial_system.service.userService;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/getSystemInfo")
public class sysController {
    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    userService userService;
    @RequestMapping("/getIncom")
    @ResponseBody
    public String SendIcomeInfo(){

        HttpSession httpSession=httpServletRequest.getSession();
        Object userobject=httpSession.getAttribute("user");
        user user=(user) userobject;
        JSONObject jsonObject=new JSONObject();
        if(getCookieUserid("role").equals("超级管理员")){
            jsonObject.put("incomInfo",userService.getIncomeList(user.getHouseid()));
        }else{
            if(userService.getIncome(user.getHouseid(),Integer.valueOf(getCookieUserid("userID")))!=null){
                jsonObject.put("incomInfo",userService.getIncome(user.getHouseid(),Integer.valueOf(getCookieUserid("userID"))));
            }else{
                jsonObject.put("incomInfo","无法获取数据库数据");
                return JSONObject.toJSONString(jsonObject);
            }
        }
        return JSONObject.toJSONString(jsonObject);


    }
    @RequestMapping("/setIncome")
    @ResponseBody
    public String SetIncome(String totelIncome){
        if(userService.setIncome(totelIncome,null,null,Integer.valueOf(getCookieUserid("userID")),1)){
            return "123";
        }

        return "444";
    }
    @RequestMapping("/getSpending")
    @ResponseBody
    public String updatebBill() throws IOException {

        JSONObject jsonObject=new JSONObject();
        HttpSession httpSession = httpServletRequest.getSession();
        Object userobject=httpSession.getAttribute("user");
        user user=(user) userobject;
        int normalUser=0;
        List<bill> billList=new ArrayList<>();
        if(getCookieUserid("role").equals("超级管理员")){
            billList=userService.getBill(user.getHouseid(),null);
        }else{
            billList=userService.getBill(user.getHouseid(),Integer.valueOf(getCookieUserid("userID")));
        }

        JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new com.family_financial_system.util.JsonDateValueProcessor());
		JSONArray jsonArray = JSONArray.fromObject(billList,jsonConfig);
        if(billList!=null){
            return jsonArray.toString();
        }
        jsonObject.put("msg","数据更新失败");
        return JSONObject.toJSONString(jsonObject);
    }

    @RequestMapping("/getType")
    @ResponseBody
    public String getType(){
        JSONArray jsonArray=JSONArray.fromObject(userService.getType());
        return jsonArray.toString();
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
