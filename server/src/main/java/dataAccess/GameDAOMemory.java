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

  public void joinGame(String username, String color, int gameID) throws DataAccessException {
    if (!this.games.containsKey(gameID)) {
      throw new DataAccessException("Error", 400);
    }
    GameData game = this.games.get(gameID);
    if (color != null && color.equals("BLACK") && game.blackUsername() != null) {
      throw new DataAccessException("Error", 403);
    } else if (color != null && color.equals("WHITE") && game.whiteUsername() != null) {
      throw new DataAccessException("Error", 403);
    }
    ChessGame chessGame = game.game();
    if (color == null) {
      this.games.replace(gameID, new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame));
    } else if (color.equals("BLACK")) {
      this.games.replace(gameID, new GameData(gameID, game.whiteUsername(), username, game.gameName(), chessGame));
    } else if (color.equals("WHITE")) {
      this.games.replace(gameID, new GameData(gameID, username, game.blackUsername(), game.gameName(), chessGame));
    }





  }

  public Collection<GameData> listGames() throws DataAccessException {
    return this.games.values();
  }
}
