package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity // 1. Говорим Spring Boot, что этот класс — сущность БД
@Table(name = "SUBSCRIBERS") // 2. ПРИВЕЛИ К ВЕРХНЕМУ РЕГИСТРУ ORACLE
public class Subscriber {

    @Id // 3. Указываем, что MSISDN — это уникальный первичный ключ (Primary Key)
    @Column(name = "MSISDN") // ПРИВЕЛИ К ВЕРХНЕМУ РЕГИСТРУ ORACLE
    private long msisdn;

    @Column(name = "PLAN_ID") // ПРИВЕЛИ К ВЕРХНЕМУ РЕГИСТРУ ORACLE
    private int planId;

    @Column(name = "BALANCE") // ПРИВЕЛИ К ВЕРХНЕМУ РЕГИСТРУ ORACLE
    private BigDecimal balance;

    // ==========================================
    // КОНСТРУКТОРЫ (Остаются без изменений)
    // ==========================================
    public Subscriber() {
    }

    public Subscriber(long msisdn, int planId, BigDecimal balance) {
        this.msisdn = msisdn;
        this.planId = planId;
        setBalance(balance);
    }

    // ==========================================
    // ГЕТТЕРЫ И СЕТТЕРЫ (Остаются без изменений)
    // ==========================================
    public long getMsisdn() {
        return msisdn;
    }

    public int getPlanId() {
        return planId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setMsisdn(long msisdn) {
        this.msisdn = msisdn;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public void setBalance(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("КРИТИЧЕСКАЯ ОШИБКА: Попытка установить отрицательный баланс в обход шлюза!");
        } else {
            this.balance = balance;
        }
    }
}
