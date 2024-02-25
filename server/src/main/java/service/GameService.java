package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.util.HashMap;

public class GameService {
  private final GameDAO gameDAO;

  public GameService(GameDAO gameDAO) {
    this.gameDAO = gameDAO;
  }

  public HashMap<Integer, GameData> listGames() throws DataAccessException {
    return this.gameDAO.listGames();
  }
  public void deleteAllGames() throws DataAccessException {
    this.gameDAO.clear();
  }
}
