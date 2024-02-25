package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;

public class GameService {
  private final GameDAO gameDAO;

  public GameService(GameDAO gameDAO) {
    this.gameDAO = gameDAO;
  }
  public void deleteAllGames() throws DataAccessException {
    this.gameDAO.clear();
  }
}
