package com.family_financial_system.service;

import com.family_financial_system.bean.bill;
import com.family_financial_system.util.baiduPicApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class pic2JsonService {
    @Autowired
    userService userService;
    private baiduPicApi picApi;
    public List<bill> picDetection(byte[] pic){
        picApi=new baiduPicApi();
        List<String> typeList=userService.getType();
        return picApi.pic2ApiJson(pic,typeList);
    }
}
