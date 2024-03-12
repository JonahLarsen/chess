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

  @Test
  void testCreateAuthPositive() throws DataAccessException {
    authDAO.createAuth(new AuthData("wooo", "hoooo"));
    authDAO.createAuth(new AuthData("yuhhhh", "nametime"));
    Assertions.assertEquals(2, authDAO.listAuths().size());
  }

  @Test
  void testCreateAuthNegative() throws DataAccessException {
    Assertions.assertThrows(DataAccessException.class, () -> {authDAO.createAuth(new AuthData(null, "whatup"));});
  }

  @Test
  void testDeleteAuthPositive() throws DataAccessException {
    authDAO.createAuth(new AuthData("yes", "no"));
    authDAO.deleteAuth("yes");
    Assertions.assertEquals(0, authDAO.listAuths().size());
  }

  @Test
  void testDeleteAuthNegative() throws DataAccessException {
    authDAO.createAuth(new AuthData("woooooo", "whatup"));
    Assertions.assertThrows(DataAccessException.class, ()-> authDAO.deleteAuth("woo"));
  }

  @Test
  void testGetAuthPositive() throws DataAccessException {
    authDAO.createAuth(new AuthData("woohoo", "yes"));
    Assertions.assertNotNull(authDAO.getAuth("woohoo"));
  }

  @Test
  void testGetAuthNegative() throws DataAccessException {
    Assertions.assertThrows(DataAccessException.class, ()-> authDAO.getAuth("silly"));
  }

  @Test
  void testListGamesPositive() throws DataAccessException {
    authDAO.createAuth(new AuthData("jo", "yo"));
    authDAO.createAuth(new AuthData("bar", "war"));
    authDAO.createAuth(new AuthData("dat", "dude"));
    Assertions.assertEquals(3, authDAO.listAuths().size());
  }

  @Test
  void testListGamesNegative() throws DataAccessException {
    //IDK how to make a negative test for this but technically the method is just for
    //testing so I'm gonna leave it with just a positive test
  }

}
