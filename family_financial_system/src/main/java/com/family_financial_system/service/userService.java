package com.family_financial_system.service;

import com.baidubce.services.bos.BosClient;
import com.family_financial_system.bean.*;
import com.family_financial_system.dao.userDao;
import com.family_financial_system.util.JedisPoolUtils;
import com.family_financial_system.util.StringUtils;
import com.family_financial_system.util.baiduBos;
import com.family_financial_system.util.baiduPicApi;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class userService {


    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    userDao userDao;
    public user login(String username, String password, Model model){
        HttpSession httpSession = httpServletRequest.getSession();
        user login=userDao.login(username,password);
        if(login!=null){
            httpSession.setAttribute("user",login);
            model.addAttribute("user",login);
            return login;
        }
        return login;
    }
    public boolean RestPassword(String password,String username){
        if(userDao.RestPassword(password,username)==1){
            return true;
        }
        return false;
    }
    public boolean AddFamilyMember(String realname,String username,String password,Integer houseid){
        user user=new user();
        user.setId(null);
        user.setRealname(realname);
        user.setUsername(username);
        user.setPassword(password);
        user.setRoleid(1);
        user.setHouseid(houseid);
        user.setPhoto(null);
        if(userDao.AddFamilyMember(user)==1){
            return true;
        }
        return false;
    }
    public List<user> getMemberList(Integer houseid){
        List<user> userList=userDao.getMemberList(houseid);
        if(userList!=null){
            return userList;
        }
        return null;

    }
    public List<user> getMainUserInfo(Integer id){
        List<user> user=userDao.getMainUserInfo(id);
        if(user!=null){
            return user;
        }
        return null;
    }
    public List<bill> getBill(Integer houseid,Integer userid){
        List<bill> billList=userDao.getBill(houseid,userid);
        if(billList!=null){
            return billList;
        }
        return null;
    }
    public boolean setIncome(String totelIncome,String yearSpending,String monthSpending,Integer userid,Integer houseid){
        System.out.println(userDao.setIncome(totelIncome,yearSpending,monthSpending,userid,houseid));
        if(userDao.setIncome(totelIncome,yearSpending,monthSpending,userid,houseid)==1){
            return true;
        }
        return false;
    }

    public income getIncome(Integer houseid,Integer userid){
        income income=userDao.getIncome(houseid,userid);
        if (income!=null){
            return income;
        }else{
            userDao.addPersonIncome(new income(null,houseid,null,null,null,null,null,userid));
        }
        return null;
    }
    public income getIncomeList(Integer houseid){
        List<income> incomeList=userDao.getIncomeList(houseid);
        int familyAssets=0;
        if(incomeList!=null){
            for(int i=0;i<incomeList.size();i++){
                if(incomeList.get(i).getUserid()!=1){
                    familyAssets=Integer.parseInt(incomeList.get(i).getTotelIncome())+familyAssets;
                }
            }
            if(incomeList.get(0).getTotelIncome()==null||familyAssets!=Integer.parseInt(incomeList.get(0).getTotelIncome())){
                userDao.setIncome(String.valueOf(familyAssets),null,null,1,1);
                incomeList.get(0).setTotelIncome(String.valueOf(familyAssets));
            }
            income income=incomeList.get(0);
            return new income(income.getId(),income.getHouseid(),income.getTotelIncome(),income.getBestIncome(),income.getLowerIncome(),income.getYearSpending(),income.getMonthSpending(),income.getUserid());
        }
        return null;
    }
    public boolean addBill(String title, Integer userid, String moneyType, Double money, Integer typeid, String remark, Integer paywayid, Timestamp time){
        billinfo billinfo=new billinfo();
        billinfo.setId(null);
        billinfo.setTitle(title);
        billinfo.setUserid(userid);
        billinfo.setMoneytype(moneyType);
        billinfo.setMoney(money);
        billinfo.setTypeid(typeid);
        billinfo.setRemark(remark);
        billinfo.setPaywayid(paywayid);
        billinfo.setTime(time);
        return userDao.addBill(billinfo) == 1;
    }
    public boolean updatebill(String title,Double money,Integer typeid,String remark,Integer paywayid,String time,Integer GoodsID) throws ParseException {
        if(title!=null||money!=null||typeid!=null||remark!=null||paywayid!=null||time!=null){
            userDao.updatebill(title,money,typeid,remark,paywayid,time!=null?new Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mmm:ss").parse(time).getTime()):null,GoodsID);
            return true;
        }
        return false;
    }

    public Integer delBill(List<String> billIndex,boolean flag){
        if (flag){
            return userDao.delbill(billIndex);
        }else {
            return 0;
        }
    }
    public List<String> getType(){
        return userDao.getType();
    }
    public List<type> getTypeList(){
        return userDao.getTypeList();
    }
    public List<payway> getPayWayList(){
        return userDao.getPayWayList();
    }

    public void inputMessageData(List<message> messages){
        userDao.messageData(messages);
    }

    public List<message> getChatHistory(String sender, String receiver){
        List<message> messageList=null;
        List<message> messageList2=null;
        try{
            Jedis jedis= JedisPoolUtils.getJedis();
            messageList=userDao.getChatHistory(Integer.valueOf(sender),Integer.valueOf(receiver));
            messageList2=userDao.getChatHistory(Integer.valueOf(receiver),Integer.valueOf(sender));
            message message=null;
            JSONArray jsonArray =null;
            if(jedis.get(sender+"."+receiver+"message")!=null&&JSONArray.fromObject(jedis.get(sender+"."+receiver+"message")).size()==messageList.size()+messageList2.size()){
                messageList=null;
                System.out.println("获取Redis缓存聊天记录");
            }else{
                jedis.del(sender+"."+receiver+"message");
                jedis.del(receiver+"."+sender+"message");
                if(messageList.size()!=0&&messageList2.size()!=0)
                {
                    for(int i=0;i<messageList2.size();i++){
                        for(int j=0;j<messageList.size();j++){
                            if (messageList.get(messageList.size()-1).getId()<messageList2.get(i).getId()){
                                message=messageList2.get(i);
                                messageList.add(message);
                                break;
                            }
                            else if(messageList.get(j).getId()>messageList2.get(i).getId()){
                                message=messageList2.get(i);
                                messageList.add(j,message);
                                break;
                            }

                        }
                    }
                    jsonArray=JSONArray.fromObject(messageList);
                }else{
                    if(messageList.size()==0){
                        jsonArray=JSONArray.fromObject(messageList2);
                        messageList2.clear();
                    }else{
                        jsonArray=JSONArray.fromObject(messageList);
                        messageList.clear();
                    }
                }

                if (jsonArray!=null){
                    jedis.set(sender+"."+receiver+"message",jsonArray.toString());
                    jedis.set(receiver+"."+sender+"message",jsonArray.toString());
                }
                System.out.println("更新聊天记录");

            }


        }catch (Exception e){
            e.printStackTrace();
            System.out.println("数据库不存在或数据库连接失败无法获取数据");
        }
        return messageList;
    }
    public String getUserAvatar(String userName){
        baiduBos baiduBos=new baiduBos();
        BosClient client=baiduBos.BosClientService();
        String picName=baiduBos.SearchPicNameOrNot(client,"user-avatar-bucket",userName);
        if(picName!=null){
            return baiduBos.generatePresignedUrl(client,"user-avatar-bucket",picName);
        }
        return null;

    }

}
