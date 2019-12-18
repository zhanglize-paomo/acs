package com.example.asc;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan(value = "com.example")
public class AscApplication {

    private static final Logger logger = LoggerFactory.getLogger(AscApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AscApplication.class, args);
		logger.info("项目启动成功");
	}

}
