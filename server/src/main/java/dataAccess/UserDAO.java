package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {

  Collection<UserData> listUsers() throws DataAccessException;

  void createUser(UserData user) throws DataAccessException;
  UserData getUser(String username) throws DataAccessException;
  void clear() throws DataAccessException;

  boolean passwordCheck(String userPassword, String realPassword) throws DataAccessException;
}
