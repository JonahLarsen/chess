package dataAccess;

import java.sql.SQLException;

public class ConfigureDatabase {

  static void configureDatabase(String[] createStatements) throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("error", 500);
    }
  }
}
