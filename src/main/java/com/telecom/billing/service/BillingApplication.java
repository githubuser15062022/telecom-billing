package com.telecom.billing.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.telecom.billing"})          // Сканируем контроллер и бизнес-логику по новому пути
@EntityScan(basePackages = {"com.telecom.billing.model"})        // Сканируем доменную модель Subscriber в новой папке
@EnableJpaRepositories(basePackages = {"com.telecom.billing.repository"}) // Сканируем твой JPA-репозиторий по новому адресу
public class BillingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingApplication.class, args);
    }
}
