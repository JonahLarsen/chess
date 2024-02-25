package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAOMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.GameService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameServiceTest {
  static final GameService gameService = new GameService(new GameDAOMemory());

  @BeforeEach
  void setUp() throws DataAccessException {
    gameService.deleteAllGames();
  }
  @Test
  public void testDeleteAllGames() throws DataAccessException {
    //TODO: Add Several Games
    gameService.deleteAllGames();
    assertEquals(0, gameService.listGames().size());
  }
}
