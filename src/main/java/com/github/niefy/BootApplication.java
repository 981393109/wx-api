package com.github.niefy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
@EnableCaching
//@SpringBootApplication(scanBasePackages = {"com.github.niefy.*"})
@MapperScan({"com.github.niefy.modules.*.mapper","com.github.niefy.modules.*.dao"})
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

}
