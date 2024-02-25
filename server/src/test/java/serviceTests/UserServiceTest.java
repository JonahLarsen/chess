package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.UserDAOMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {
  static final UserService userService = new UserService(new UserDAOMemory());
  @BeforeEach
  void setUp () throws DataAccessException {
    userService.deleteAllUsers();
  }

  @Test
  void testDeleteAllUsers () throws DataAccessException {
    //TODO: Add several Users
    userService.deleteAllUsers();
    assertEquals(0, userService.listUsers().size());
  }
}
