package service;

import chess.ChessGame;
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

  public void joinGame(String username, String color, int gameID) throws DataAccessException {
    this.gameDAO.joinGame(username, color, gameID);
  }
  public int createGame(String gameName) throws DataAccessException {
    if (gameName == null) {
      throw new DataAccessException("Error", 400);
    }
    return this.gameDAO.createGame(gameName);
  }
  public void deleteAllGames() throws DataAccessException {
    this.gameDAO.clear();
  }
}
