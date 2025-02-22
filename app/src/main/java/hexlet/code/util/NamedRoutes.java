package hexlet.code.util;

public class NamedRoutes {
    private static final String URLS = "/urls";
    private static final String TESTDB = "/testdb";

    public static String urlsPath() {
        return URLS;
    }

    public static String urlsPath(String id) {
        return URLS + "/" + id;
    }

    public static String testdbPath() {
        return TESTDB;
    }



    /*private static final String USERS = "/users";
    private static final String COURSES = "/courses";
    private static final String BUILD = "/build";
    private static final String EDIT = "/edit";
    private static final String SESSIONS = "/sessions";

    public static String usersPath() {
        return USERS;
    }

    public static String usersPath(String id) {
        return USERS + "/" + id;
    }

    public static String usersPath(Long id) {
        return usersPath(String.valueOf(id));
    }

    public static String buildUsersPath() {
        return USERS + BUILD;
    }

    public static String editUserPath(String id) {
        return USERS + "/" + id + EDIT;
    }

    public static String editUserPath(Long id) {
        return editUserPath(String.valueOf(id));
    }

    public static String coursesPath() {
        return COURSES;
    }

    public static String coursePath(String id) {
        return COURSES + "/" + id;
    }

    public static String coursePath(Long id) {
        return coursePath(String.valueOf(id));
    }

    public static String buildCoursesPath() {
        return COURSES + BUILD;
    }

    public static String editCoursePath(String id) {
        return COURSES + "/" + id + EDIT;
    }

    public static String editCoursePath(Long id) {
        return editCoursePath(String.valueOf(id));
    }

    public static String sessionsPath() {
        return SESSIONS;
    }

    public static String buildSessionsPath() {
        return SESSIONS + "/build";
    }

    public static String destroySessionsPath() {
        return SESSIONS + "/destroy";
    }*/

}
