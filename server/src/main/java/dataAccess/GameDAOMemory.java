package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAOMemory implements GameDAO {
  private final HashMap<Integer, GameData> games = new HashMap<>();

  public void clear() throws DataAccessException {
    this.games.clear();
  }

  public HashMap<Integer, GameData> listGames() throws DataAccessException {
    return this.games;
  }
}
