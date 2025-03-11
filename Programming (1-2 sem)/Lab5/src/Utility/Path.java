package Utility;

/**
 * Управляет переменной окружения для парсинга history
 * @author sh_ub
 */
public class Path {
    /**
     * Возвращает путь в зависимости от того, где запускается прога: локально или на гелиосе
     */
    public static String getJsonPath() {
        String jsonPath = System.getenv("JSON_PATH");

        if (jsonPath == null || jsonPath.isEmpty()) {
            jsonPath = "/home/studs/s468125/history.txt"; // Локальный путь по умолчанию
        }

        return jsonPath;
    }
}