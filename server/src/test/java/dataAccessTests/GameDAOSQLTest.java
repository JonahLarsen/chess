package dataAccessTests;

import dataAccess.AuthDAOSQL;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.GameDAOSQL;
import org.junit.jupiter.api.BeforeEach;

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
}
