package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
  static final GameDAOSQL gameDAO = new GameDAOSQL();
  static final GameService gameService = new GameService(gameDAO);
  static final AuthService authService = new AuthService(new AuthDAOSQL());

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
    //This test is actually not really that good but I check for authorization
    // in the handler rather than the Service so there's no other way to get an error.
    assertThrows(DataAccessException.class, ()->authService.getAuth(null));
  }

  @Test
  public void testCreateGamePositive() throws DataAccessException {
    gameService.createGame("game of chess woohoo");
    gameService.createGame("chessssssss");
    assertEquals(2, gameService.listGames().size());
  }

  @Test
  public void testCreateGameNegative() throws DataAccessException {
    assertThrows(DataAccessException.class, ()-> gameService.createGame(null));
  }

  @Test
  public void testJoinGamePositive() throws DataAccessException {
    int gameID = gameService.createGame("chessgame");
    assertDoesNotThrow(()-> gameService.joinGame("username", "BLACK", gameID));
  }

  @Test
  public void testJoinGameNegative() throws DataAccessException {
    int gameID = gameService.createGame("chess");
    assertThrows(DataAccessException.class, () -> gameService.joinGame("username", "BLACK", gameID + 5));
  }
}
