package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {

  void insertUser(UserData u) throws DataAccessException;
  Collection<UserData> listUsers() throws DataAccessException;

  void createUser(UserData user) throws DataAccessException;
  UserData getUser(String username) throws DataAccessException;
  void clear() throws DataAccessException;
}
