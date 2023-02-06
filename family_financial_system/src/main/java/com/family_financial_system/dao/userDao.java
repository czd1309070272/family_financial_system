package com.family_financial_system.dao;

import com.family_financial_system.bean.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface userDao {
    public user login(@Param("username") String username,@Param("password") String password);
    public int RestPassword(@Param("password") String password,@Param("UserOrMail") String UserOrMail);
    public int AddFamilyMember(user user);
    public List<user> getMemberList(@Param("houseid") Integer houseid);
    public List<bill> getBill(@Param("houseid") Integer houseid,@Param("userid") Integer userid);
    public int addPersonIncome(income income);
    public int setIncome(@Param("totelIncome") String totelIncome,@Param("yearSpending") String yearSpending,@Param("monthSpending") String monthSpending,@Param("userid") Integer userid,@Param("houseid") Integer houseid);
    public income getIncome(@Param("houseid") Integer houseid,@Param("userid") Integer userid);
    public List<income> getIncomeList(@Param("houseid") Integer houseid);
    public int addBill(billinfo billinfo);
    public int updatebill(@Param("title") String title, @Param("money") Double money, @Param("typeid") Integer typeid, @Param("remark") String remark, @Param("paywayid") Integer paywayid, @Param("time") Timestamp time, @Param("id") Integer id);
    public int delbill(List<String> billIndex);
    public List<user> getMainUserInfo(@Param("id") Integer id);
    public List<String> getType();
    public List<type> getTypeList();
    public List<payway> getPayWayList();
    public int messageData(List<message> messages);
    public List<message> getChatHistory(@Param("sender") Integer sender,@Param("receiver") Integer receiver);
    public int createUserChatHistory(@Param("tableName") String tableName);


    public testUser loginIn(@Param("username") String username, @Param("password") String password);
}
