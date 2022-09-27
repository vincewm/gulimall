package com.vince.gulimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GuliMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuliMemberApplication.class, args);
		System.err.println("Member已启动");
	}

}
