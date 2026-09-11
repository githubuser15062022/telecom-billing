package com.telecom.billing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/subscribers")
public class BillingRestController {

    // 1. Переменная для твоего сервиса валидации
    private final BillingValidationService billingValidationService;

    // 2. Инструмент Spring для асинхронной отправки сообщений в брокер Kafka
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // 3. Внедряем сервис через конструктор (Dependency Injection)
    public BillingRestController(BillingValidationService billingValidationService) {
        this.billingValidationService = billingValidationService;
    }

    /**
     * Сетевой эндпоинт для удаленной валидации абонента по HTTP POST
     * Принимает JSON вида: { "msisdn": 79991112233 }
     */
    @PostMapping("/validate")
    public ResponseEntity<String> validateSubscriber(@RequestBody Map<String, Long> requestBody) {

        Long msisdn = requestBody.get("msisdn");

        if (msisdn == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка 400: В JSON-пакете отсутствует обязательное поле 'msisdn'");
        }

        try {
            System.out.println("\n🌐 СЕТЕВОЙ ВЫЗОВ: Запрос на валидацию абонента через REST API для " + msisdn);

            // Твоя базовая логика работы с СУБД Oracle
            this.billingValidationService.registerSubscriberFromDatabase(msisdn);
            BillingValidationService.validateBalanceAndCountry(msisdn);

            // ===================================================================
            // 🔥 АСИНХРОННЫЙ СТРИМ В KAFKA (Требование вакансии TenChat)
            // ===================================================================
            // Если код дошел до этой строки, значит абонент успешно прошел все проверки!
            String eventMessage = "Абонент " + msisdn + " успешно верифицирован интеграционным шлюзом в СУБД Oracle.";

            // Отправляем в топик "billing-events", где ключом будет номер (msisdn), а значением - лог-сообщение
            kafkaTemplate.send("billing-events", String.valueOf(msisdn), eventMessage);
            // ===================================================================

            return ResponseEntity.ok("✅ Абонент " + msisdn + " успешно верифицирован. Событие отправлено в Kafka.");

        } catch (BillingValidationException e) {
            System.err.println("❌ ОТКЛОНЕНО ШЛЮЗОМ: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Бизнес-запрет биллинга: " + e.getMessage());

        } catch (Exception e) {
            System.err.println("❌ КРИТИЧЕСКИЙ СБОЙ REST СЛОЯ: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка шлюза: " + e.getMessage());
        }
    }
}
