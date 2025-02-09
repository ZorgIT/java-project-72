package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class Database {
    private static final HikariDataSource DATA_SOURCE;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getenv().getOrDefault("JDBC_DATABASE_URL",
                "jdbc:postgresql://dpg-cuihqe52ng1s73dk69qg-a.frankfurt-postgres.render.com:5432/hexlet_hw_db"));
        config.setUsername(System.getenv().getOrDefault("DB_USERNAME", "user"));
        config.setPassword(System.getenv().getOrDefault("DB_PASSWORD", "pass"));


        //TODO убрать временные пароли на ENV файл

        //
        // При необходимости можно задать драйвер явно
        config.setDriverClassName("org.postgresql.Driver");

        // Дополнительные параметры пула:
        config.setMaximumPoolSize(10); // максимальное число соединений
        config.setMinimumIdle(2);      // минимальное число "живых" соединений

        // Создаем DataSource на основе конфигурации
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
