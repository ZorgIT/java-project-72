package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.util.Optional;

public class Database {
    private static final HikariDataSource DATA_SOURCE;

    static {
        // Конфигурируем dotenv так, чтобы игнорировать отсутствие файла .env
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        HikariConfig config = new HikariConfig();

        // Получаем JDBC URL из системных переменных или из .env
        String jdbcUrl = Optional.ofNullable(System.getenv("JDBC_DATABASE_URL"))
                .filter(s -> !s.isEmpty())
                .orElse(dotenv.get("JDBC_DATABASE_URL", ""));
        if (jdbcUrl.isEmpty()) {
            throw new IllegalStateException("JDBC_DATABASE_URL не задан. "
                    + "Укажите необходимые параметры подключения в системных "
                    + "переменных или в файле .env.");
        }
        config.setJdbcUrl(jdbcUrl);

        // Получаем имя пользователя
        String username = Optional.ofNullable(System.getenv("DB_USERNAME"))
                .filter(s -> !s.isEmpty())
                .orElse(dotenv.get("DB_USERNAME", ""));
        if (username.isEmpty()) {
            throw new IllegalStateException("DB_USERNAME не задан. "
                    + "Укажите имя пользователя для подключения к базе данных"
                    + ".");
        }
        config.setUsername(username);

        // Получаем пароль
        String password = Optional.ofNullable(System.getenv("DB_PASSWORD"))
                .filter(s -> !s.isEmpty())
                .orElse(dotenv.get("DB_PASSWORD", ""));
        if (password.isEmpty()) {
            throw new IllegalStateException("DB_PASSWORD не задан. "
                    + "Укажите пароль для подключения к базе данных.");
        }
        config.setPassword(password);

        // Устанавливаем драйвер PostgreSQL
        config.setDriverClassName("org.postgresql.Driver");

        // Настройки пула соединений
        config.setMaximumPoolSize(10); // максимальное число соединений
        config.setMinimumIdle(2);      // минимальное число "живых" соединений

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
