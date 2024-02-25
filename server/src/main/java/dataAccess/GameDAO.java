package dataAccess;

import model.GameData;
import java.util.HashMap;

public interface GameDAO {
  void clear() throws DataAccessException;

  HashMap<Integer, GameData> listGames() throws DataAccessException;
}
