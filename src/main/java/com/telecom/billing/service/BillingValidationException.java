package com.telecom.billing.service;

/**
 * Кастомное исключение для аварийных ситуаций в биллинге
 */
public class BillingValidationException extends RuntimeException {

    // Конструктор, который принимает текст ошибки
    public BillingValidationException(String message) {
        super(message); // Передаем текст в родительский класс Java
    }
}