package com.family_financial_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.family_financial_system.dao")
public class FamilyFinancialSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FamilyFinancialSystemApplication.class, args);
	}

}
