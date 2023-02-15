package com.bank.bankaccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class BankaccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankaccountApplication.class, args);
	}
/*
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));   // It will set UTC timezone
		System.out.println("Spring boot application running in UTC timezone :" + new Date());   // It will print UTC timezone
	}
*/
	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}


}
