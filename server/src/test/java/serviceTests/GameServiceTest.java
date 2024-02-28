package serviceTests;

import dataAccess.AuthDAOMemory;
import dataAccess.DataAccessException;
import dataAccess.GameDAOMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTest {
  static final GameService gameService = new GameService(new GameDAOMemory());
  static final AuthService authService = new AuthService(new AuthDAOMemory());

  @BeforeEach
  void setUp() throws DataAccessException {
    gameService.deleteAllGames();
  }
  @Test
  public void testDeleteAllGames() throws DataAccessException {
    gameService.createGame("Chess Time");
    gameService.createGame("Chess Time Again");
    gameService.deleteAllGames();
    assertEquals(0, gameService.listGames().size());
  }

  @Test
  public void testListGamesPositive() throws DataAccessException {
    gameService.createGame("game of chess yo");
    gameService.createGame("chess time");
    assertEquals(2, gameService.listGames().size());
  }

  @Test
  public void testListGamesNegative() throws DataAccessException {
    gameService.createGame("game time");
    gameService.createGame("chess woohoo");
    //This test is actually not really that good but I check for authorization in the handler rather than the Service so there's no other way to get an error.
    assertThrows(DataAccessException.class, ()->authService.getAuth(null));
  }

  @Test
  public void

}
