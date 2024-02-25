package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public interface AuthDAO{
  void clear() throws DataAccessException;

  Collection<AuthData> listAuths() throws DataAccessException;
}