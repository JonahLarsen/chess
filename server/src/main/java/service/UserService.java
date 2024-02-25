package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Collection;

public class UserService {
  private final UserDAO userDAO;

  public UserService(UserDAO userDAO) {
    this.userDAO = userDAO;
  }
  public AuthData register(UserData user) {return null;}
  public AuthData login(UserData user) {return null;}
  public void logout(UserData user) {}

  public Collection<UserData> listUsers() throws DataAccessException {
    return this.userDAO.listUsers();
  }
  public void deleteAllUsers() throws DataAccessException {
    this.userDAO.clear();

  }
}
