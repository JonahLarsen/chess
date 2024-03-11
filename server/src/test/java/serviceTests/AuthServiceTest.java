package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.UserService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AuthServiceTest {
  static AuthService authService;
  public AuthServiceTest() {
    try {
      authService = new AuthService(new AuthDAOSQL());
    } catch (DataAccessException e) {
      System.out.println("Can't create test class");
    }

  }


  static final UserService userService = new UserService(new UserDAOMemory());

  @BeforeEach
  void setUp() throws DataAccessException {
    authService.deleteAllAuth();
    userService.deleteAllUsers();
  }
  @Test
  public void testDeleteAllAuth() throws DataAccessException {
    authService.createAuth(new AuthData("authtokentoken", "username"));
    authService.createAuth(new AuthData("token to prove I'm logged in", "user"));
    authService.deleteAllAuth();
    assertEquals(0, authService.listAuths().size());
  }

  @Test
  public void testLogoutPositive() throws DataAccessException {
    AuthData userAuth = userService.register(new UserData("username", "password", "email"));
    authService.createAuth(userAuth);
    authService.logout(userAuth.authToken());
    assertEquals(0, authService.listAuths().size());
  }

  @Test
  public void testLogoutNegative() throws DataAccessException {
    assertThrows(DataAccessException.class, ()-> authService.logout("ThisIsNotARealAuthToken"));
  }
}
