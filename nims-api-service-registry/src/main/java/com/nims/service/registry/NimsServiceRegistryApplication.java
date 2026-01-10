package com.nims.service.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class NimsServiceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(NimsServiceRegistryApplication.class, args);
    }

}
