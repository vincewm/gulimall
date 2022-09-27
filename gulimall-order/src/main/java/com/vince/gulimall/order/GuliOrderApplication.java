package com.vince.gulimall.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GuliOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuliOrderApplication.class, args);
		System.err.println("订单模块已启动");
	}

}
