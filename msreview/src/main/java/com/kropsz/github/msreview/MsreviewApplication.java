package com.kropsz.github.msreview;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class MsreviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsreviewApplication.class, args);
	}

}
