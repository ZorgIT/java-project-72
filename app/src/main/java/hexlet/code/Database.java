package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.util.Optional;

public class Database {
    private static final HikariDataSource DATA_SOURCE;

    static {

        String envValue = Optional.ofNullable(System.getenv("env"))
                .orElse(System.getProperty("env", ""))
                .toLowerCase();

        boolean isTest = "test".equals(envValue);


        Dotenv dotenv = Dotenv.configure()
                .directory(isTest ? "src/test/resources" : ".")
                .ignoreIfMissing()
                .load();


        String jdbcUrlFromProp = System.getProperty("JDBC_DATABASE_URL");
        String jdbcUrlFromEnv = System.getenv("JDBC_DATABASE_URL");
        String jdbcUrlFromDotenv = dotenv.get("JDBC_DATABASE_URL", "");


        String userFromProp = System.getProperty("DB_USERNAME");
        String userFromEnv = System.getenv("DB_USERNAME");
        String userFromDotenv = dotenv.get("DB_USERNAME", "");


        String passFromProp = System.getProperty("DB_PASSWORD");
        String passFromEnv = System.getenv("DB_PASSWORD");
        String passFromDotenv = dotenv.get("DB_PASSWORD", "");


        String jdbcUrl = firstNonEmpty(jdbcUrlFromProp, jdbcUrlFromEnv, jdbcUrlFromDotenv);
        String dbUser = firstNonEmpty(userFromProp, userFromEnv, userFromDotenv);
        String dbPass = firstNonEmpty(passFromProp, passFromEnv, passFromDotenv);


        HikariConfig config = new HikariConfig();

        boolean urlLooksLikeH2 = jdbcUrl.toLowerCase().startsWith("jdbc:h2:");
        boolean useH2 = isTest || jdbcUrl.isEmpty() || urlLooksLikeH2;

        if (useH2) {

            jdbcUrl = "jdbc:h2:mem:project";

            config.setJdbcUrl(jdbcUrl);

            config.setUsername("");
            config.setPassword("");

            config.setDriverClassName("org.h2.Driver");
            config.setPoolName("H2Pool");
            config.setMaximumPoolSize(5);
            config.setConnectionInitSql("SELECT 1");
            System.out.println("Используется H2 (In-Memory Database): " + jdbcUrl);
        } else {

            if (dbUser.isEmpty() || dbPass.isEmpty()) {
                jdbcUrl = "jdbc:h2:mem:project";

                config.setJdbcUrl(jdbcUrl);

                config.setUsername("");
                config.setPassword("");

                config.setDriverClassName("org.h2.Driver");
                config.setPoolName("H2Pool");
                config.setMaximumPoolSize(5);
                config.setConnectionInitSql("SELECT 1");
                System.out.println("Используется H2 (In-Memory Database): " + jdbcUrl);
            } else {
                config.setJdbcUrl(jdbcUrl);
                config.setUsername(dbUser);
                config.setPassword(dbPass);
                config.setDriverClassName("org.postgresql.Driver");
                config.setMaximumPoolSize(10);
                config.setMinimumIdle(2);
                System.out.println("Используется PostgreSQL: " + jdbcUrl);
            }
        }


        DATA_SOURCE = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }

    public static void closeDataSource() {
        if (DATA_SOURCE != null && !DATA_SOURCE.isClosed()) {
            DATA_SOURCE.close();
        }
    }


    private static String firstNonEmpty(String... variants) {
        for (String v : variants) {
            if (v != null && !v.isEmpty()) {
                return v;
            }
        }
        return "";
    }
}
