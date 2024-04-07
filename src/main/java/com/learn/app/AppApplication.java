package com.learn.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.learn.app", "com.learn.app.Config", "com.learn.app.Controllers"})
public class AppApplication {



	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}


}
