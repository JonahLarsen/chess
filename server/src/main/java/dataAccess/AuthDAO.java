package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public interface AuthDAO{
  void clear() throws DataAccessException;
  void createAuth(AuthData auth) throws DataAccessException;

  HashMap<String, AuthData> listAuths() throws DataAccessException;
}