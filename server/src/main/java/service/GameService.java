package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameService {
  private final GameDAO gameDAO;

  public GameService(GameDAO gameDAO) {
    this.gameDAO = gameDAO;
  }

  public Collection<GameData> listGames() throws DataAccessException {
    return this.gameDAO.listGames();
  }

  public int createGame(String gameName) throws DataAccessException {
    return this.gameDAO.createGame(gameName);
  }
  public void deleteAllGames() throws DataAccessException {
    this.gameDAO.clear();
  }
}
