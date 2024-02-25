package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class AuthDAOMemory implements AuthDAO {
  private final ArrayList<AuthData> authTokens = new ArrayList<>();

  public void clear() throws DataAccessException {
    this.authTokens.clear();
  }

  public Collection<AuthData> listAuths() throws DataAccessException {
    return this.authTokens;
  }


}
