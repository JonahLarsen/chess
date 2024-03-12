package dataAccessTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDAOSQLTest {
  UserDAO userDAO;
  public UserDAOSQLTest() {
    try {
      userDAO = new UserDAOSQL();
    } catch (DataAccessException e) {
      System.out.println("Unable to instantiate AuthDAO");
    }
  }

  @BeforeEach
  void setUp() throws DataAccessException {
    userDAO.clear();
  }

  @Test
  void testClear() throws DataAccessException {
    userDAO.createUser(new UserData("user", "pass", "email"));
    userDAO.createUser(new UserData("user2", "userspass", "mailtime"));
    userDAO.clear();
    Assertions.assertEquals(0, userDAO.listUsers().size());
  }

  @Test
  void testCreateUserPositive() throws DataAccessException {
    userDAO.createUser(new UserData("user", "password", "email"));
    Assertions.assertEquals(1, userDAO.listUsers().size());
  }

  @Test
  void testCreateUserNegative() throws DataAccessException {
    Assertions.assertThrows(DataAccessException.class, ()-> userDAO.createUser(new UserData(null, "pass", "email")));
  }

  @Test
  void testGetUserPositive() throws DataAccessException {
    userDAO.createUser(new UserData("user", "pass", "email"));
    Assertions.assertDoesNotThrow(()-> userDAO.getUser("user"));
  }

  @Test
  void testGetUserNegative() throws DataAccessException {
    Assertions.assertEquals(null, userDAO.getUser(null));
  }

  @Test
  void testListUsersPositive() throws DataAccessException {
    userDAO.createUser(new UserData("user", "pass", "email"));
    Assertions.assertEquals(1, userDAO.listUsers().size());
  }

  @Test
  void testListUsersNegative() throws DataAccessException {
    //Not sure how to do a negative test for this one
  }
}
