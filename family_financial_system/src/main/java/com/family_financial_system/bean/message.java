package com.family_financial_system.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class message {
    Integer id;
    String message;
    Integer Sender;
    Integer Receiver;
    Timestamp msgtime;
}
