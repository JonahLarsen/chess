package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;


public class UserDAOMemory implements UserDAO{
  private final HashSet<UserData> users = new HashSet<>();

  public void insertUser(UserData u) throws DataAccessException {  }

  public Collection<UserData> listUsers() throws DataAccessException {
    return this.users;
  }

  public void clear() throws DataAccessException {
    users.clear();
  }
}
