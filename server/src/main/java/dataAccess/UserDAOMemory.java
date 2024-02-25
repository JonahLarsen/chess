package dataAccess;

import model.UserData;

import java.util.HashSet;


public class UserDAOMemory implements UserDAO{
  private final HashSet<UserData> users = new HashSet<>();

  public void insertUser(UserData u) throws DataAccessException {  }

  public void clear() throws DataAccessException {
    users.clear();
  }
}
