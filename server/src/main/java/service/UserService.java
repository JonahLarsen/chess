package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.UUID;

public class UserService {
  private final UserDAO userDAO;

  public UserService(UserDAO userDAO) {
    this.userDAO = userDAO;
  }
  public AuthData register(UserData user) throws DataAccessException {
    String username = user.username();
    UserData existingUser = this.getUser(username);
    if (existingUser == null) {
      this.createUser(user);
      return new AuthData(UUID.randomUUID().toString(), username);
    } else {
      throw new DataAccessException("Error: already taken", 403);
    }

  }
  public AuthData login(UserData user) {

  }
  public void logout(UserData user) {}

  public void createUser(UserData user) throws DataAccessException {
    this.userDAO.createUser(user);
  }
  public UserData getUser(String username) throws DataAccessException {

    return this.userDAO.getUser(username);
  }
  public Collection<UserData> listUsers() throws DataAccessException {
    return this.userDAO.listUsers();
  }
  public void deleteAllUsers() throws DataAccessException {
    this.userDAO.clear();

  }
}
