package service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/subscribers")
public class BillingRestController {

    // 1. Объявляем переменную для нашего сервиса
    private final BillingValidationService billingValidationService;

    // 2. Внедряем сервис через конструктор (Dependency Injection)
    // Spring Boot сам создаст объект BillingValidationService и передаст его сюда
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

            // ИСПРАВЛЕНО: Теперь вызываем метод у внедренного ОБЪЕКТА (с маленькой буквы), а не у класса!
            this.billingValidationService.registerSubscriberFromDatabase(msisdn);

            // Тут метод остался статическим в сервисе, поэтому его вызываем через имя класса
            BillingValidationService.validateBalanceAndCountry(msisdn);

            return ResponseEntity.ok("✅ Абонент " + msisdn + " успешно верифицирован интеграционным шлюзом.");

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