package dataAccess;

import model.GameData;

import java.util.Collection;

public class GameDAOSQL implements GameDAO{
  public void clear() throws DataAccessException {

  }

  public int createGame(String gameName) throws DataAccessException {
    return 0;
  }

  public void joinGame(String username, String color, int gameID) throws DataAccessException {

  }

  public Collection<GameData> listGames() throws DataAccessException {
    return null;
  }
}
