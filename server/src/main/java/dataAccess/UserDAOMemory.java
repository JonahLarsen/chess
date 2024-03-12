package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;


public class UserDAOMemory implements UserDAO{
  private final HashMap<String, UserData> users = new HashMap<>();


  public Collection<UserData> listUsers() throws DataAccessException {
    return this.users.values();
  }

  public void createUser(UserData user) throws DataAccessException {
    this.users.put(user.username(), user);
  }
  public UserData getUser(String username) throws DataAccessException {
    return this.users.get(username);
  }
  public void clear() throws DataAccessException {
    users.clear();
  }

  public boolean passwordCheck(String userPassword, String realPassword) throws DataAccessException {
    return userPassword.equals(realPassword);
  }
}
