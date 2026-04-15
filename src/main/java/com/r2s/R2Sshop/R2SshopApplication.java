package com.r2s.R2Sshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class R2SshopApplication {
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
	}
	public static void main(String[] args) {
		SpringApplication.run(R2SshopApplication.class, args);
	}
}
