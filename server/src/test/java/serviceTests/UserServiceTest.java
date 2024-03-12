package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
  static AuthService authService;
  static UserService userService;
  public UserServiceTest() {
    try {
      authService = new AuthService(new AuthDAOSQL());
      userService = new UserService(new UserDAOSQL());
    } catch (DataAccessException e) {
      System.out.println("Can't create test class");
    }
  }
  @BeforeEach
  void setUp () throws DataAccessException {
    userService.deleteAllUsers();
    authService.deleteAllAuth();
  }

  @Test
  void testDeleteAllUsers () throws DataAccessException {
    userService.createUser(new UserData("username", "password", "email"));
    userService.createUser(new UserData("user", "pass", "mail"));
    userService.deleteAllUsers();
    assertEquals(0, userService.listUsers().size());
  }

  @Test
  void testRegisterPositive() throws DataAccessException {
    AuthData authToken = userService.register(new UserData("user", "pass", "email"));
    assertNotNull(authToken);
  }

  @Test
  void testRegisterNegative() throws DataAccessException {
    userService.register(new UserData("user123", "password", "email"));
    assertThrows(DataAccessException.class, () -> userService.register(new UserData("user123", "password", "email")));
  }

  @Test
  void testLoginPositive() throws DataAccessException {
    AuthData auth = userService.register(new UserData("username", "password", "email"));
    authService.createAuth(auth);
    authService.logout(auth.authToken());
    AuthData newAuth = userService.login(new UserData("username", "password", "email"));
    assertNotNull(newAuth);
  }

  @Test
  void testLoginNegative() throws DataAccessException {
    userService.register(new UserData("username", "password", "email"));
    assertThrows(DataAccessException.class, () -> userService.register(new UserData("username", "password", "email")));
  }
}
