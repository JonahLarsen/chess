package dataAccessTests;

import dataAccess.AuthDAOSQL;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.GameDAOSQL;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class GameDAOSQLTest {

  GameDAO gameDAO;
  public GameDAOSQLTest() {
    try {
      gameDAO = new GameDAOSQL();
    } catch (DataAccessException e) {
      System.out.println("Unable to instantiate AuthDAO");
    }
  }

  @BeforeEach
  void setUp() throws DataAccessException {
    gameDAO.clear();
  }

  @Test
  void testClear() throws DataAccessException {
    gameDAO.createGame("game1");
    gameDAO.createGame("game2");
    gameDAO.clear();
    Assertions.assertEquals(0, gameDAO.listGames().size());
  }

  @Test
  void testCreateGamePositive() throws DataAccessException {
    gameDAO.createGame("game1");
    gameDAO.createGame("game10000");
    Assertions.assertEquals(2, gameDAO.listGames().size());
  }

  @Test
  void testCreateGameNegative() throws DataAccessException {
    Assertions.assertThrows(DataAccessException.class, ()-> gameDAO.createGame(null));
  }

  @Test
  void testJoinGamePositive() throws DataAccessException {
    int id = gameDAO.createGame("game1");
    Assertions.assertDoesNotThrow(()->gameDAO.joinGame("user", "BLACK", id));
  }

  @Test
  void testJoinGameNegative() throws DataAccessException {
    Assertions.assertThrows(DataAccessException.class, ()->gameDAO.joinGame("user", "WHITE", 50));
  }

  @Test
  void testListGamesPositive() throws DataAccessException {
    gameDAO.createGame("game1");
    gameDAO.createGame("game2");
    gameDAO.createGame("game3");
    Assertions.assertEquals(3, gameDAO.listGames().size());
  }

  @Test
  void testListGamesNegative() throws DataAccessException {
    gameDAO.createGame("game1");
    //Not sure how to create a negative test for this one
  }

}
