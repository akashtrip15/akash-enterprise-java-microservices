package com.nims.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class NimsPatientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NimsPatientServiceApplication.class, args);
    }

}
