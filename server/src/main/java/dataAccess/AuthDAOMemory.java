package dataAccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthDAOMemory {
  private final ArrayList<AuthData> authTokens = new ArrayList<>();

  public void clear() throws DataAccessException {
    this.authTokens.clear();
  }
}
