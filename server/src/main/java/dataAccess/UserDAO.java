package dataAccess;

import model.UserData;

public interface UserDAO {

  void insertUser(UserData u) throws DataAccessException;


  void clear() throws DataAccessException;
}
