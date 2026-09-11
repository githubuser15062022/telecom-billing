import service.DatabaseConnectionManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) {
        System.out.println("=== Запуск Шлюза: Тестируем Парсинг Ошибок Oracle ===");

        // Открываем TCP-сессию к твоей базе petproject7
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {

            System.out.println("ИНФО: Сетевое соединение установлено. Провоцируем ошибку СУБД...");

            // Намеренно пытаемся создать таблицу, которая УЖЕ ЕСТЬ в базе.
            // Это гарантированно вызовет аварию в ядре Oracle!
            String провокацияSql = "CREATE TABLE subscribers (id NUMBER)";
            statement.executeUpdate(провокацияSql);

            System.out.println("УПС: Если ты видишь эту строку, значит таблица почему-то создалась. Такого быть не должно!");

        } catch (SQLException e) {
            System.out.println("\n🚨 ВНИМАНИЕ: СУБД Оракл выдала системный сбой! Перехватываем пакет...");

            // 1. Вытаскиваем текстовое продовое сообщение
            String полнаяОшибка = e.getMessage();
            // 2. Вытаскиваем чистый числовой код ошибки от ядра Oracle (без приставки ORA-)
            int кодОшибкиOracle = e.getErrorCode();
            // 3. Вытаскиваем международный статус SQLState
            String sqlСостояние = e.getSQLState();

            System.out.println("--------------------------------------------------");
            System.out.println("Текст из лога: " + полнаяОшибка);
            System.out.println("Вытащенный код ошибки (getErrorCode()): " + кодОшибкиOracle);
            System.out.println("Международный SQLState: " + sqlСостояние);
            System.out.println("--------------------------------------------------");

            // 4. Включаем логику Self-Healing (Авто-восстановления системы) на основе кода
            if (кодОшибкиOracle == 955) { // 955 — это ORA-00955
                System.out.println("✅ АНАЛИЗ SRE: Инцидент классифицирован. Причина: Таблица 'subscribers' уже создана ранее.");
                System.out.println("✅ РЕШЕНИЕ ШЛЮЗА: Игнорируем ошибку 955, инфраструктура исправна, продолжаем работу биллинга!");
            }
            else if (кодОшибкиOracle == 1017) { // ORA-01017
                System.out.println("❌ АНАЛИЗ БЕЗОПАСНОСТИ: Неверный пароль к схеме SYSTEM! Бьем тревогу в Grafana!");
            }
            else if (кодОшибкиOracle == 1109) { // ORA-01109
                System.out.println("❌ АНАЛИЗ ИНФРАСТРУКТУРЫ: База данных закрыта (MOUNTED). Требуется ручной ALTER DATABASE OPEN!");
            }
            else {
                System.out.println("❌ КРИТИЧЕСКИЙ СБОЙ: Неизвестная авария СУБД. Стопаем конвейер.");
            }
        }
HashMap<Integer, Integer> map = new HashMap<>();
        map.put(10,0);
        map.put(20,1);
        if (map.containsKey(10)) {
            System.out.println("Индекс числа 10 в мапе: " + map.get(10));
        }
        int[] nums = {2,7,11,15};
        int target = 9;

        HashMap<Integer, Integer> twinMap = new HashMap<>();
        int[] result = new int[2];

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];

            if (twinMap.containsKey(complement)) {
                result[0] = twinMap.get(complement);
                result[1] = i;
                break;
            }
        twinMap.put(nums[i], i);


        }
        System.out.println("Результать Two Sum: [" + result[0] + "," + result[1] + "}");



        System.out.println("\n=== Программа завершила работу в штатном режиме ===");
    }
}
