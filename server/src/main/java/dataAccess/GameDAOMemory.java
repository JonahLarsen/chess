package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAOMemory implements GameDAO {
  private final HashMap<Integer, GameData> games = new HashMap<>();
  private int nextID = 0;

  public void clear() throws DataAccessException {
    this.games.clear();
  }
  public int createGame(String gameName) throws DataAccessException {
    nextID++;
    this.games.put(nextID, new GameData(nextID, null, null, gameName, new ChessGame()));
    return nextID;

  }
  public Collection<GameData> listGames() throws DataAccessException {
    return this.games.values();
  }
}
