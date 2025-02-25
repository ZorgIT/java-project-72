package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class Database {
    private static final HikariDataSource DATA_SOURCE;

    static {
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");
        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            jdbcUrl = System.getProperty("JDBC_DATABASE_URL", "");
        }

        HikariConfig config = new HikariConfig();

        if (jdbcUrl.isEmpty()) {

            config.setJdbcUrl("jdbc:h2:mem:project");
            config.setUsername("");
            config.setPassword("");
            config.setDriverClassName("org.h2.Driver");
            config.setMaximumPoolSize(5);
            config.setConnectionInitSql("SELECT 1");
            System.out.println("Используется H2: jdbc:h2:mem:project");
        } else if (jdbcUrl.toLowerCase().startsWith("jdbc:h2:")) {

            config.setJdbcUrl(jdbcUrl);
            config.setUsername("");
            config.setPassword("");
            config.setDriverClassName("org.h2.Driver");
            config.setMaximumPoolSize(5);
            config.setConnectionInitSql("SELECT 1");
            System.out.println("Используется H2: " + jdbcUrl);
        } else {

            config.setJdbcUrl(jdbcUrl);
            String dbUser = System.getenv("DB_USERNAME");
            String dbPass = System.getenv("DB_PASSWORD");
            if (dbUser == null) {
                dbUser = System.getProperty("DB_USERNAME", "");
            }
            if (dbPass == null) {
                dbPass = System.getProperty("DB_PASSWORD", "");
            }
            config.setUsername(dbUser);
            config.setPassword(dbPass);
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
