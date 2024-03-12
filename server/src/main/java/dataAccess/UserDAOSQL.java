package dataAccess;

import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class UserDAOSQL implements UserDAO{

  public UserDAOSQL() throws DataAccessException {
    configureDatabase();
  }

  public Collection<UserData> listUsers() throws DataAccessException {
    ArrayList<UserData> result = new ArrayList<>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, password, email FROM user";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        try (var response = preparedStatement.executeQuery()) {
          while (response.next()) {
            String name = response.getString("username");
            String password = response.getString("password");
            String email = response.getString("email");
            result.add(new UserData(name, password, email));
          }
          return result;
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
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
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, password, email FROM user WHERE username = ?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, username);
        try (var response = preparedStatement.executeQuery()) {
          if (response.next()) {
            String name = response.getString("username");
            String password = response.getString("password");
            String email = response.getString("email");
            return new UserData(name, password, email);
          } else {
            return null;
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 401);
    }
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
