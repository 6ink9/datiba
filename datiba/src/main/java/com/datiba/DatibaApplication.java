package com.datiba;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages = "com.datiba.mapper")
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DatibaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatibaApplication.class, args);
    }

}
