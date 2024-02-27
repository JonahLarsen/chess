package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;


public class AuthService {
  private final AuthDAO authDAO;

  public AuthService(AuthDAO authDAO) {
    this.authDAO = authDAO;
  }

  public void createAuth(AuthData auth) throws DataAccessException {
    this.authDAO.createAuth(auth);
  }
  public void logout(String authToken) throws DataAccessException {
    this.authDAO.deleteAuth(authToken);
  }

  public AuthData getAuth(String authToken) throws DataAccessException {
     return this.authDAO.getAuth(authToken);
  }
  public HashMap<String, AuthData> listAuths() throws DataAccessException {
    return this.authDAO.listAuths();
  }
  public void deleteAllAuth() throws DataAccessException {
    this.authDAO.clear();
  }
}

