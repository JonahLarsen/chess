package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AuthDAOSQL implements AuthDAO{

  public AuthDAOSQL() throws DataAccessException {
    configureDatabase();
  }

  public void clear() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "TRUNCATE auth";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
  }

  public void createAuth(AuthData auth) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, auth.authToken());
        preparedStatement.setString(2, auth.username());
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }


  }

  public void deleteAuth(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "DELETE FROM auth WHERE authToken=?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, authToken);
        int changed = preparedStatement.executeUpdate();
        if (changed != 1) {
          throw new SQLException();
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 401);
    }
  }


  public AuthData getAuth(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT authToken, username FROM auth WHERE authToken = ?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, authToken);
        try (var response = preparedStatement.executeQuery()) {
          if (response.next()) {
            String token = response.getString("authToken");
            String username = response.getString("username");
            return new AuthData(token, username);
          } else {
            throw new SQLException();
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Error", 401);
    }
  }

  public HashMap<String, AuthData> listAuths() throws DataAccessException {
    HashMap<String, AuthData> result = new HashMap<>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT authToken, username FROM auth";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        try (var response = preparedStatement.executeQuery()) {
          while (response.next()) {
            String token = response.getString("authToken");
            String username = response.getString("username");
            result.put(token, new AuthData(token, username));
          }
          return result;
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
  }

  private final String[] createStatements = {
    """
    CREATE TABLE IF NOT EXISTS auth (
      authToken varchar(256) NOT NULL,
      username varchar(256) NOT NULL,
      PRIMARY KEY (authToken)
      ) 
    """
  };


  private void configureDatabase() throws DataAccessException {
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
