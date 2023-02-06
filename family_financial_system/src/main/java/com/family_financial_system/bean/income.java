package com.family_financial_system.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class income {
    Integer id;
    Integer houseid;
    String totelIncome;
    String bestIncome;
    String lowerIncome;
    String yearSpending;
    String monthSpending;
    Integer userid;
}
