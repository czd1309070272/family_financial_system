package com.family_financial_system.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidubce.services.bos.BosClient;
import com.family_financial_system.bean.*;
import com.family_financial_system.service.pic2JsonService;
import com.family_financial_system.service.userService;
import com.family_financial_system.util.JedisPoolUtils;
import com.family_financial_system.util.baiduBos;
import com.family_financial_system.util.baiduPicApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import redis.clients.jedis.Jedis;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class picReceiveController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    pic2JsonService pic2JsonService;
    @Autowired
    userService userService;
    private Jedis jedis= JedisPoolUtils.getJedis();
    @PostMapping("/uploadFile")
    public@ResponseBody String uploadFile(@Validated UploadParamsDto uploadParamsDto,MultipartHttpServletRequest request) throws IOException {
        MultipartFile file=uploadParamsDto.getFile();
        List<bill> billList=new ArrayList<>();
        if(file.getBytes()!=null){
            billList=pic2JsonService.picDetection(file.getBytes());
        }
        com.alibaba.fastjson.JSONArray jsonArray= com.alibaba.fastjson.JSONArray.parseArray(JSON.toJSONString(billList));
        jedis.set("tempBillList",jsonArray.toString());

        return "123";

    }
    @PostMapping("/uploadAvatar")
    public@ResponseBody String uploadAvatar(@Validated UploadParamsDto uploadParamsDto,MultipartHttpServletRequest request) throws IOException {
        MultipartFile file=uploadParamsDto.getFile();
        baiduBos baiduBos=new baiduBos();
        BosClient client=baiduBos.BosClientService();
        HttpSession httpSession = httpServletRequest.getSession();
        Object userobject=httpSession.getAttribute("user");
        user user=(user) userobject;
        if(file.getBytes()!=null){
            baiduBos.PutObject(client,"user-avatar-bucket",""+user.getRealname()+".gif",file.getBytes());
        }

//        com.alibaba.fastjson.JSONArray jsonArray= com.alibaba.fastjson.JSONArray.parseArray(JSON.toJSONString(billList));
//        jedis.set("tempBillList",jsonArray.toString());

        return "123";

    }
    @RequestMapping("/getTempBillList")
    @ResponseBody
    public String getTempBillList() throws JsonProcessingException {
        String bill=(String)jedis.get("tempBillList");
        jedis.del("tempBillList");
        JSONArray array = JSONArray.parseArray(bill);
        List<bill> billList = JSONObject.parseArray(array.toJSONString(), bill.class);
        return JSON.parse(JSONObject.toJSONStringWithDateFormat(billList,"yyyy-MM-dd HH:mm:ss")).toString();
    }
    @RequestMapping(value = "/upload2DB",method = RequestMethod.POST)
    @ResponseBody
    public String upload2DB(@Param("dataSet") String dataSet){
        JSONArray array=JSONArray.parseArray(dataSet);
        List<bill> billList = JSONObject.parseArray(array.toJSONString(), bill.class);
        List<type> typeList=userService.getTypeList();
        List<payway> paywayList=userService.getPayWayList();
        for(int i=0;i<billList.size();i++){
            for(int j=0;j<typeList.size();j++){
                if(billList.get(i).getName().equals(typeList.get(j).getName())){
                    billList.get(i).setName(String.valueOf(typeList.get(j).getId()));
                }
            }
            for(int j=0;j<paywayList.size();j++){
                if(billList.get(i).getPayway().equals(paywayList.get(j).getPayway())){
                    billList.get(i).setPayway(String.valueOf(paywayList.get(j).getId()));
                }
            }
            bill bill=billList.get(i);
            if(bill.getRemark().equals("")){
                bill.setRemark(null);
            }
//            if(userService.addBill(bill.getTitle(),user.getId(),bill.getMoneytype(),Double.valueOf(bill.getMoney().toString()),Integer.valueOf(bill.getName()),bill.getRemark(),Integer.valueOf(bill.getPayway()),bill.getTime())){
//                return "200";
//            }
        }
        return ""+billList.size()+"";
    }


}
