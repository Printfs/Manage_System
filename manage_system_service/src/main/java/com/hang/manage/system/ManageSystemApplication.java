package com.hang.manage.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@EnableAsync  //开启异步注解功能
@EnableSwagger2
@SpringBootApplication
@MapperScan(basePackages = "com.hang.manage.system.mapper")
public class ManageSystemApplication {
    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(ManageSystemApplication.class,args);
    }

}
