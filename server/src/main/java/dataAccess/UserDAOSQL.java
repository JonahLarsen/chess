package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.Collection;

public class UserDAOSQL implements UserDAO{
  public void insertUser(UserData u) throws DataAccessException {

  }

  public Collection<UserData> listUsers() throws DataAccessException {
    return null;
  }

  public void createUser(UserData user) throws DataAccessException {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashedPassword = encoder.encode(user.password());
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, user.username());
        preparedStatement.setString(2, hashedPassword);
        preparedStatement.setString(3, user.email());
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
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

  public boolean passwordCheck(String userPassword, String realPassword) throws DataAccessException {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(userPassword, realPassword);
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
