package com.family_financial_system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family_financial_system.bean.user;
import com.family_financial_system.service.userService;
import com.family_financial_system.util.JedisPoolUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;
    @RequestMapping("/")
    public String loginPage(){
        return "login";
    }
    @Autowired
    userService userService;
    @RequestMapping(value = "/loginIn",method = RequestMethod.POST)
    public String loginIn(String username, String password, Model model){
        user user=userService.login(username,password,model);
        if(user!=null){
            Cookie cookie1=new Cookie("cookieHouseID", String.valueOf(user.getHouseid()));
            Cookie cookie2=new Cookie("userID", String.valueOf(user.getId()));
            cookie1.setMaxAge(-1);
            cookie2.setMaxAge(-1);
            httpServletResponse.addCookie(cookie1);
            httpServletResponse.addCookie(cookie2);
            switch (user.getRoleid()){
                case 0:model.addAttribute("role","超级管理员");break;
                case 1:model.addAttribute("role","户主");break;
                case 2:model.addAttribute("role","家庭成员");break;
            }
            Cookie cookie=new Cookie("role", String.valueOf(model.getAttribute("role")));
            cookie.setMaxAge(-1);
            httpServletResponse.addCookie(cookie);
            return "index";
        }
        model.addAttribute("msg","用户名或密码错误,请重试!");
        return "login";
    }
    @RequestMapping(value = "restPwd",method = RequestMethod.POST)
    public String RestPassword(){
        if(userService.RestPassword("123","root")){
            return "login";
        }
        return "forgetPassword";
    }
    @RequestMapping(value = "/addMember",method = RequestMethod.POST)
    public String AddFamilyMember(String realname,String mamberUsername,String mamberPassword){
        String houseid="";
        Cookie[] cookies=httpServletRequest.getCookies();
        for(int i=0;i< cookies.length;i++){
            Cookie cookie=cookies[i];
            if(cookie.getName().equals("cookieHouseID")){
                houseid=cookie.getValue();
            }
        }
        if(userService.AddFamilyMember(realname,mamberUsername,mamberPassword,Integer.valueOf(houseid))){
            return "clients";
        }
        System.out.println("添加失败");
        return "1";
    }

}
