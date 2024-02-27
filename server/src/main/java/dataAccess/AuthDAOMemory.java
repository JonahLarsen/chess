package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AuthDAOMemory implements AuthDAO {
  private final HashMap<String, AuthData> authTokens = new HashMap<>();

  public void clear() throws DataAccessException {
    this.authTokens.clear();
  }

  public void deleteAuth(String authToken) throws DataAccessException {
    if (this.authTokens.containsKey(authToken)) {
      this.authTokens.remove(authToken);
    } else {
      throw new DataAccessException("Error", 401);
    }

  }
  public void createAuth(AuthData auth) throws DataAccessException {
    this.authTokens.put(auth.authToken(), auth);
  }
  public HashMap<String, AuthData> listAuths() throws DataAccessException {
    return this.authTokens;
  }


}
