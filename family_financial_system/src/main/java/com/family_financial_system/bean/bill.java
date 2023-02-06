package com.family_financial_system.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class bill {
    Integer id;
    String title;
    String realname;
    String name;
    String moneytype;
    BigDecimal money;
    String payway;
    Timestamp time;
    String remark;
    Integer houseid;
}
