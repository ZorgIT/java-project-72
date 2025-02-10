package hexlet.code.repositories;

import hexlet.code.models.Url;

import javax.sql.DataSource;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class UrlRepository extends BaseRepository {

    public UrlRepository(DataSource dataSource) {
        super(dataSource);
    }

    public void createTable() {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS urls ("
                             + "id BIGSERIAL PRIMARY KEY, "
                             + "name VARCHAR(255) NOT NULL, "
                             + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
             )) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы urls", e);
        }
    }

    public void save(Url url) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO urls (name, created_at) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS
             )) {
            stmt.setString(1, url.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(url.getCreatedAt()));
            stmt.executeUpdate();

            // Получаем сгенерированный идентификатор
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    url.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения URL", e);
        }
    }

    public List<Url> findAll() {
        List<Url> urls = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM urls")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Url url = new Url(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                urls.add(url);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка URL", e);
        }
        return urls;
    }
}