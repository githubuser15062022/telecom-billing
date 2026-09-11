package com.telecom.billing.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Инфраструктурный менеджер для управления сессиями СУБД Oracle
 */
public class DatabaseConnectionManager {

    // Указываем параметры твоей локальной Oracle XE
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/petproject7";
    private static final String USER = "SYSTEM";
    private static final String PASSWORD = "0508"; // ВСТАВЬ СВОЙ ПАРОЛЬ ОТ ORACLE

    /**
     * Метод возвращает активное физическое соединение с базой данных
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
