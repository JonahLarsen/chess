package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.HashMap;

public class AuthDAOSQL implements AuthDAO{

  public AuthDAOSQL() {
    try {
      configureDatabase();
    } catch (DataAccessException e) {

    }

  }

  public void clear() throws DataAccessException {

  }

  public void createAuth(AuthData auth) throws DataAccessException {

  }

  public void deleteAuth(String authToken) throws DataAccessException {

  }

  public AuthData getAuth(String authToken) throws DataAccessException {
    return null;
  }

  public HashMap<String, AuthData> listAuths() throws DataAccessException {
    return null;
  }

  private final String[] createStatements = {
    """
    CREATE TABLE IF NOT EXISTS auth (
      authToken int NOT NULL,
      username varchar(256) NOT NULL,
      PRIMARY KEY (authToken),
      FOREIGN KEY (username) references user(username)
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
