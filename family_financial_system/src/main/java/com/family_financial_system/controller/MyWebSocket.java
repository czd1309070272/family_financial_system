package com.family_financial_system.controller;


import com.family_financial_system.bean.message;
import com.family_financial_system.service.userService;
import com.family_financial_system.util.JedisPoolUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{nickname}")
@Component
public class MyWebSocket {
    //用来存放每个客户端对应的MyWebSocket对象。
    int num=0;
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    private List<String> messageSave=new ArrayList<String>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String nickname;
    private String sender;
    private String receiver;
    List<String> onlineUser=new ArrayList<String>();
    @Autowired
    userService userService;
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("nickname") String nickname) {
        this.session = session;
        this.nickname=nickname.substring(0,nickname.indexOf(":"));
        webSocketSet.add(this);     //加入set中
//        this.session.getAsyncRemote().sendText(this.session.toString());
        this.session.getAsyncRemote().sendText("恭喜您成功连接上WebSocket-->当前在线人数为："+webSocketSet.size());
        System.out.println("有新连接加入:"+this.nickname+",当前在线人数为" + webSocketSet.size()+"websockset:"+webSocketSet);


    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        System.out.println("有一连接关闭！当前在线人数为" + webSocketSet.size());




    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session,@PathParam("nickname") String nickname) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String msgTime = df.format(new Timestamp(System.currentTimeMillis()));
        sender=nickname.substring(nickname.indexOf(":")+1,nickname.indexOf("."));
        receiver=nickname.substring(nickname.indexOf(".")+1);
        num++;
        System.out.println("来自客户端的消息-->"+sender+": " + message);
        //群发消息
        String mess="{"+"\"id\":"+num+","+"\"message\":"+"\""+message+"\""+","+"\"Sender\":"+"\""+sender+"\""+","+"\"Receiver\":"+"\""+receiver+"\""+","+"\"MsgTime\":"+"\""+msgTime+"\""+"}";
        messageSave.add(mess);


        List<String> saveMsg=new ArrayList<String>();
        Jedis jedis= JedisPoolUtils.getJedis();
        if(jedis.get("temporalMessageData")==null){
            saveMsg.add(mess);
            jedis.set("temporalMessageData",saveMsg.toString());
        }else{
            JSONArray jsonArray=new JSONArray(jedis.get("temporalMessageData"));
            for(int i=0;i<jsonArray.length();i++){
                saveMsg.add(jsonArray.getJSONObject(i).toString());
            }
            saveMsg.add(mess);
            jedis.set("temporalMessageData",saveMsg.toString());
        }
        saveMsg.clear();


        broadcast(mess,sender,receiver);
    }
    /**
     * 发生错误时调用
     *
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }
    /**
     * 群发自定义消息
     * */
    public void broadcast(String message,String sender,String receiver){
        for (MyWebSocket item : webSocketSet) {
            if (item.nickname.equals(receiver)){
                item.session.getAsyncRemote().sendText(message);//异步发送消息.
            }
            if(item.nickname.equals(sender)){
                item.session.getAsyncRemote().sendText(message);//异步发送消息.
            }


        }
    }
}
