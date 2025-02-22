package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.util.Optional;

public class Database {
    private static final HikariDataSource DATA_SOURCE;

    static {
        boolean isTest = "test".equals(System.getProperty("env"));
        Dotenv dotenv = Dotenv.configure()
                .directory(isTest ? "src/test/resources" : ".")
                .ignoreIfMissing()
                .load();

        HikariConfig config = new HikariConfig();

        String jdbcUrl = Optional.ofNullable(System.getenv("JDBC_DATABASE_URL"))
                .filter(s -> !s.isEmpty())
                .orElse(dotenv.get("JDBC_DATABASE_URL", "jdbc:h2:mem:project;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"));

        // Для H2 разрешаем URL без явного указания в переменных окружения
        if (jdbcUrl.isEmpty()) {
            // Для тестов используем отдельную in-memory базу
            jdbcUrl = "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false";
        }
        config.setJdbcUrl(jdbcUrl);

        boolean isH2 = jdbcUrl.startsWith("jdbc:h2:");

        // Настройки для H2
        String defaultH2User = "sa";
        String defaultH2Password = "";

        // Получаем имя пользователя с учетом типа БД
        String username = Optional.ofNullable(System.getenv("DB_USERNAME"))
                .filter(s -> !s.isEmpty())
                .orElseGet(() -> dotenv.get("DB_USERNAME", isH2 ? defaultH2User : ""));

        // Для H2 используем дефолтные значения, если не указаны
        if (username.isEmpty() && isH2) {
            username = defaultH2User;
        }

        // Получаем пароль с учетом типа БД
        String password = Optional.ofNullable(System.getenv("DB_PASSWORD"))
                .filter(s -> !s.isEmpty())
                .orElseGet(() -> dotenv.get("DB_PASSWORD", isH2 ? defaultH2Password : ""));

        // Проверяем учетные данные только для PostgreSQL
        if (!isH2) {
            if (username.isEmpty()) {
                throw new IllegalStateException("DB_USERNAME не задан для PostgreSQL");
            }
            if (password.isEmpty()) {
                throw new IllegalStateException("DB_PASSWORD не задан для PostgreSQL");
            }
        }

        config.setUsername(username);
        config.setPassword(password);

        // Устанавливаем драйвер в зависимости от типа БД
        if (isH2) {
            config.setDriverClassName("org.h2.Driver");
        } else if (jdbcUrl.startsWith("jdbc:postgresql:")) {
            config.setDriverClassName("org.postgresql.Driver");
        } else {
            throw new IllegalArgumentException("Unsupported JDBC URL: " + jdbcUrl);
        }

        // Оптимизации для H2
        if (isH2) {
            config.setPoolName("H2Pool");
            config.setMaximumPoolSize(5);
            config.setConnectionInitSql("SELECT 1");
        } else {
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
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
}
