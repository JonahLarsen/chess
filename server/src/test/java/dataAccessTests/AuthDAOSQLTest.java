package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.AuthDAOSQL;
import dataAccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthDAOSQLTest {
  AuthDAO authDAO;

  public AuthDAOSQLTest() {
    try {
      authDAO = new AuthDAOSQL();
    } catch (DataAccessException e) {
      System.out.println("Unable to instantiate AuthDAO");
    }
  }

  @BeforeEach
  void setUp() throws DataAccessException {
    authDAO.clear();
  }

  @Test
  void testClear() throws DataAccessException {
    authDAO.createAuth(new AuthData("hello", "user"));
    authDAO.createAuth(new AuthData("whatup", "name"));
    authDAO.clear();

    Assertions.assertEquals(0, authDAO.listAuths().size());
  }


}
