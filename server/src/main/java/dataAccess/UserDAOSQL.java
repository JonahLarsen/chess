package dataAccess;

import model.UserData;

import java.sql.SQLException;
import java.util.Collection;

public class UserDAOSQL implements UserDAO{
  public void insertUser(UserData u) throws DataAccessException {

  }

  public Collection<UserData> listUsers() throws DataAccessException {
    return null;
  }

  public void createUser(UserData user) throws DataAccessException {

  }

  public UserData getUser(String username) throws DataAccessException {
    return null;
  }

  public void clear() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "TRUNCATE user";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
  }

  private final String[] createStatements = {
          """
    CREATE TABLE IF NOT EXISTS user (
      username varchar(256) NOT NULL,
      password varchar(256) NOT NULL,
      email varchar(256) NOT NULL,
      PRIMARY KEY (username)
      ) 
    """
  };
  private void configureDatabase() throws DataAccessException {
    ConfigureDatabase.configureDatabase(createStatements);
  }


}
