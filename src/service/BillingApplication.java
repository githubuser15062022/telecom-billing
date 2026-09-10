package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"service"})          // Сканируем контроллер и бизнес-логику
@EntityScan(basePackages = {"model"})               // Сканируем доменную модель Subscriber
@EnableJpaRepositories(basePackages = {"repository"}) // ХАРДКОД: Принудительно сканируем твой репозиторий!
public class BillingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingApplication.class, args);
    }
}
