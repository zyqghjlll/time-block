package com.calm.tools.botfy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author Administrator
 */
@SpringBootApplication
@MapperScan("com.vwcc.agent.repository.mysql.mapper")
@EnableCaching
public class BotfyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotfyApplication.class,args);
    }

}
