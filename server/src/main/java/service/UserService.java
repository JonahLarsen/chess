package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.UUID;

public class UserService {
  private final UserDAO userDAO;

  public UserService(UserDAO userDAO) {
    this.userDAO = userDAO;
  }
  public AuthData register(UserData user) throws DataAccessException {
    UserData existingUser = this.getUser(user);
    if (existingUser == null) {
      this.createUser(user);
      return new AuthData(UUID.randomUUID().toString(), user.username());
    } else {
      throw new DataAccessException("Error: already taken", 403);
    }

  }
  public AuthData login(UserData user) throws DataAccessException {
    UserData realUser = this.getUser(user);
    if (realUser == null) {
      throw new DataAccessException("Error", 401);
    } else if (this.userDAO.passwordCheck(user.password(), realUser.password())) {
      return new AuthData(UUID.randomUUID().toString(), user.username());
    }  else {
      throw new DataAccessException("Error", 401);
    }
  }

  public void createUser(UserData user) throws DataAccessException {
    this.userDAO.createUser(user);
  }
  public UserData getUser(UserData user) throws DataAccessException {
    String username = user.username();
    return this.userDAO.getUser(username);
  }
  public Collection<UserData> listUsers() throws DataAccessException {
    return this.userDAO.listUsers();
  }
  public void deleteAllUsers() throws DataAccessException {
    this.userDAO.clear();

  }
}
