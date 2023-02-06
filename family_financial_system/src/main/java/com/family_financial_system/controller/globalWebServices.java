package com.family_financial_system.controller;

import com.alibaba.fastjson.JSON;
import com.family_financial_system.util.JedisPoolUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/webService/{user}")
@Component
public class globalWebServices {
    private static CopyOnWriteArraySet<globalWebServices> webSocketSet=new CopyOnWriteArraySet<globalWebServices>();
    private Session session;
    private Integer user;
    private Jedis jedis= JedisPoolUtils.getJedis();
    @OnOpen
    public void onOpen(Session session, @PathParam("user") String user) {
        this.session = session;
        this.user=Integer.valueOf(user);
        webSocketSet.add(this);     //加入set中
        this.session.getAsyncRemote().sendText("主服务器已连接");
        HashMap<Integer,String> userState=new HashMap<Integer,String>();

        List<String> userStateList=new ArrayList<>();

        String userStateMess=null;
        if(isEmptyRedis("userState")){
            userState.put(this.user,"online");
            jedis.set("userState", JSON.toJSONString(userState));
        }else{
            HashMap<Integer,String> hashTempMap= JSON.parseObject(jedis.get("userState"),HashMap.class);
            if(hashTempMap.get(this.user)!=null){
                hashTempMap.replace(this.user,"online");
            }else{
                hashTempMap.put(this.user,"online");
            }
            System.out.println("用户 "+this.user+" 已上线");
            jedis.set("userState", JSON.toJSONString(hashTempMap));
        }

        System.out.println("websocksetSession:"+webSocketSet);


    }
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        HashMap<Integer,String> hashTempMap= JSON.parseObject(jedis.get("userState"),HashMap.class);
        hashTempMap.replace(this.user,"offline");
        jedis.set("userState", JSON.toJSONString(hashTempMap));
        System.out.println("主服务器已关闭");
    }
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }
    public boolean isEmptyRedis(String DBname){
        if(jedis.get(DBname)==null||jedis.get(DBname)=="[]"){
            return true;
        }else{
            return false;
        }
    }
}
