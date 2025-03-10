package hexlet.code.util;

public class NamedRoutes {
    private static final String URLS = "/urls";

    public static String urlsPath() {
        return URLS;
    }

    public static String urlsPath(String id) {
        return URLS + "/" + id;
    }

    public static String urlsCheckPath(String id) {
        return URLS + "/" + id + "/checks";
    }

    // Исправленный метод для Long
    public static String urlsPath(Long urlId) {
        return urlsPath(urlId.toString()); // Преобразуем Long в String
    }
}
