package com.family_financial_system.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class user {
    Integer id;
    String username;
    String password;
    String realname;
    Integer roleid;
    Integer houseid;
    String photo;
    String email;
    Integer relationid;
    String tel;
}
