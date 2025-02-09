package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.util.Optional;

public class Database {
    private static final HikariDataSource DATA_SOURCE;

    static {
        Dotenv dotenv = Dotenv.load();
        //Get config from system if not exist go to .env
        HikariConfig config = new HikariConfig();
        String jdbcUrl = Optional.ofNullable(System.getenv("JDBC_DATABASE_URL"))
                .filter(s -> !s.isEmpty())
                .orElse(dotenv.get("JDBC_DATABASE_URL", ""));
        config.setJdbcUrl(jdbcUrl);

        String username = Optional.ofNullable(System.getenv(
                        "DB_USERNAME"))
                .filter(s -> !s.isEmpty())
                .orElse(dotenv.get("DB_USERNAME", ""));
        config.setUsername(username);

        String password = Optional.ofNullable(System.getenv(
                        "DB_PASSWORD"))
                .filter(s -> !s.isEmpty())
                .orElse(dotenv.get("DB_PASSWORD", ""));
        config.setPassword(password);

        // set driver manually
        config.setDriverClassName("org.postgresql.Driver");

        // Pull parameter
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
