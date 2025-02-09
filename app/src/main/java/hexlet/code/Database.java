package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class Database {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getenv().getOrDefault("JDBC_DATABASE_URL",
                "postgresql://hexlet_hw_db_user:6j9wmHlipr6ahtOM1N2Ft0wSK4h975w4@dpg-cuihqe52ng1s73dk69qg-a.frankfurt-postgres.render.com/hexlet_hw_db"));
        config.setUsername(System.getenv().getOrDefault("USERNAME","user"));
        config.setPassword(System.getenv().getOrDefault("PASSWORD","pass"));

        //
        // При необходимости можно задать драйвер явно
        // config.setDriverClassName("org.postgresql.Driver");

        // Дополнительные параметры пула:
        config.setMaximumPoolSize(10); // максимальное число соединений
        config.setMinimumIdle(2);      // минимальное число "живых" соединений

        // Создаем DataSource на основе конфигурации
        dataSource = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
