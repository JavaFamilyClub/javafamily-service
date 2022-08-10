package com.xxl.job.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * @author Jack Li
 * @date 2022/4/22 9:09 上午
 */
@SpringBootApplication
public class Application9018 {

	public static void main(String[] args) {
		// setting default time zone
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+08:00"));

		SpringApplication.run(Application9018.class, args);
	}

}
