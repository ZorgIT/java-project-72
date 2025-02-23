package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.util.Optional;

public class Database {
    private static final HikariDataSource DATA_SOURCE;

    static {
        // Определяем, тестовая среда или нет
        boolean isTest = "test".equals(System.getProperty("env"));

        // Загружаем переменные из .env (если есть)
        Dotenv dotenv = Dotenv.configure()
                .directory(isTest ? "src/test/resources" : ".")
                .ignoreIfMissing()
                .load();

        HikariConfig config = new HikariConfig();

        // Определяем URL базы данных
        String jdbcUrl = Optional.ofNullable(System.getenv("JDBC_DATABASE_URL"))
                .orElse(dotenv.get("JDBC_DATABASE_URL", ""));

        // Если БД явно не указана, используем H2
        boolean useH2 = isTest || jdbcUrl.isEmpty();

        if (useH2) {
            jdbcUrl = "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
            config.setJdbcUrl(jdbcUrl);
            config.setUsername("sa");
            config.setPassword("sa");
            config.setDriverClassName("org.h2.Driver");
            config.setPoolName("H2Pool");
            config.setMaximumPoolSize(5);
            config.setConnectionInitSql("SELECT 1");
            System.out.println("Используется H2 (In-Memory Database) для тестов.");
        } else {
            // Используем PostgreSQL
            String username = Optional.ofNullable(System.getenv("DB_USERNAME"))
                    .orElse(dotenv.get("DB_USERNAME", ""));
            String password = Optional.ofNullable(System.getenv("DB_PASSWORD"))
                    .orElse(dotenv.get("DB_PASSWORD", ""));

            // Проверяем, заданы ли все переменные для PostgreSQL
            if (username.isEmpty() || password.isEmpty()) {
                throw new IllegalStateException("Отсутствуют учетные данные для PostgreSQL! "
                        + "Добавьте DB_USERNAME и DB_PASSWORD.");
            }

            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName("org.postgresql.Driver");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            System.out.println("Используется PostgreSQL: " + jdbcUrl);
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
