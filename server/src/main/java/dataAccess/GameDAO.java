package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public interface GameDAO {
  void clear() throws DataAccessException;
  int createGame(String gameName) throws DataAccessException;
  Collection<GameData> listGames() throws DataAccessException;
}
