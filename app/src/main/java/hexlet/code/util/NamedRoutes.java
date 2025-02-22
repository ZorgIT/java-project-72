package hexlet.code.util;

public class NamedRoutes {
    private static final String URLS = "/urls";

    public static String urlsPath() {
        return URLS;
    }

    public static String urlsPath(String id) {
        return URLS + "/" + id;
    }

}
