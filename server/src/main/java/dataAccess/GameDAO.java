package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public interface GameDAO {
  void clear() throws DataAccessException;
  int createGame(String gameName) throws DataAccessException;
  void joinGame(String username, String color, int gameID) throws DataAccessException;
  Collection<GameData> listGames() throws DataAccessException;
  GameData getGame(int gameID) throws DataAccessException;
}
