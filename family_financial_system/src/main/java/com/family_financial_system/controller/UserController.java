package com.family_financial_system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.family_financial_system.bean.bill;
import com.family_financial_system.bean.user;
import com.family_financial_system.service.userService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/getInfo")
public class UserController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    userService userService;
    @RequestMapping("/getUserSession")
    @ResponseBody
    public String getUserSession(){
        JSONObject jsonObject=new JSONObject();
        String houseid="";
        Cookie[] cookies=httpServletRequest.getCookies();
        for(int i=0;i< cookies.length;i++){
            Cookie cookie=cookies[i];
            if(cookie.getName().equals("cookieHouseID")){
                houseid=cookie.getValue();
            }
        }
        if(userService.getMemberList(Integer.valueOf(houseid))!=null)
        {
            jsonObject.put("userList",userService.getMemberList(Integer.valueOf(houseid)));
            return JSONObject.toJSONString(jsonObject);
        }
        jsonObject.put("username","null");
        return JSONObject.toJSONString(jsonObject);
    }
    @RequestMapping("/getMainUserInfo")
    @ResponseBody
    public String getMainUserInfo(Integer id){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("mainUserInfo",userService.getMainUserInfo(id));
        return JSONObject.toJSONString(jsonObject);
    }
    @RequestMapping("/updateBill")
    @ResponseBody
    public String updatebBill(){
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
        if(billList!=null){
            return JSON.parse(JSONObject.toJSONStringWithDateFormat(billList,"yyyy-MM-dd HH:mm:ss")).toString();
        }
        jsonObject.put("msg","数据更新失败");
        return JSONObject.toJSONString(jsonObject);
    }
    @RequestMapping("/getUserAuthor")
    @ResponseBody
    public String getUserAuthor(){
        return JSONObject.toJSONString(getCookieUserid("role"));
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
    @RequestMapping("/getUserAvatar")
    @ResponseBody
    public String getUserAvatar(String username){
        JSONObject jsonObject=new JSONObject();
        String avatarUrl=userService.getUserAvatar(username);
        if(avatarUrl!=null){
            jsonObject.put("avatar",avatarUrl);
        }else{
            jsonObject.put("avatar","null");
        }

        return JSONObject.toJSONString(jsonObject);
    }
}
