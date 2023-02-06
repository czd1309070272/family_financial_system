package com.family_financial_system.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class billinfo {
    Integer id;
    String title;
    Integer userid;
    String moneytype;
    Double money;
    Integer typeid;
    String remark;
    Integer paywayid;
    Timestamp time;
}
