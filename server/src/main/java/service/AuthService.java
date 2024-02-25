package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;

import java.util.Collection;


public class AuthService {
  private final AuthDAO authDAO;

  public AuthService(AuthDAO authDAO) {
    this.authDAO = authDAO;
  }

  public Collection<AuthData> listAuths() throws DataAccessException {
    return this.authDAO.listAuths();
  }
  public void deleteAllAuth() throws DataAccessException {
    this.authDAO.clear();
  }
}

