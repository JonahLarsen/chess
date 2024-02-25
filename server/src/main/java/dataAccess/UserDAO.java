package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {

  void insertUser(UserData u) throws DataAccessException;
  Collection<UserData> listUsers() throws DataAccessException;

  void clear() throws DataAccessException;
}
