package serviceTests;

import dataAccess.AuthDAOMemory;
import dataAccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AuthServiceTest {
  static final AuthService authService = new AuthService(new AuthDAOMemory());

  @BeforeEach
  void setUp() throws DataAccessException {
    authService.deleteAllAuth();
  }
  @Test
  public void testDeleteAllAuth() throws DataAccessException {
    authService.createAuth(new AuthData("authtokentoken", "username"));
    authService.createAuth(new AuthData("token to prove I'm logged in", "user"));
    authService.deleteAllAuth();
    assertEquals(0, authService.listAuths().size());
  }
}
