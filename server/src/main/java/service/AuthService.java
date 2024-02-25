package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;


public class AuthService {
  private final AuthDAO authDAO;

  public AuthService(AuthDAO authDAO) {
    this.authDAO = authDAO;
  }
  public void deleteAllAuth() throws DataAccessException {
    this.authDAO.clear();
  }
}

