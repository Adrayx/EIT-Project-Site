package com.eit.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.eit.common.entity", "com.eit.admin.user"})
public class EitBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(EitBackEndApplication.class, args);
    }

}
