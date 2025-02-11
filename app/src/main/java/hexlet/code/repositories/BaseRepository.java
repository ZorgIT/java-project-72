package hexlet.code.repositories;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseRepository {
    public static DataSource dataSource;

    public BaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
