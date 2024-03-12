package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class GameDAOSQL implements GameDAO{

  public GameDAOSQL() throws DataAccessException {
    configureDatabase();
  }
  public void clear() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "TRUNCATE game";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
  }

  public int createGame(String gameName) throws DataAccessException {
    ChessGame newGame = new ChessGame();
    var jsonGame = new Gson().toJson(newGame);
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "INSERT INTO game (gameName, game) VALUES (?, ?)";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, gameName);
        preparedStatement.setString(2, jsonGame);
        int gameID = preparedStatement.executeUpdate();
        return gameID;
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
  }

  public void checkGameExists(int gameID) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID FROM game WHERE gameID = ?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setInt(1, gameID);
        try (var response = preparedStatement.executeQuery()) {
          if (!response.next()) {
            throw new DataAccessException("Error", 400);
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
  }
  public GameData getGame(int gameID) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setInt(1, gameID);
        try (var response = preparedStatement.executeQuery()) {
          if (response.next()) {
            int id = response.getInt("gameID");
            String whiteUsername = response.getString("whiteUsername");
            String blackUsername = response.getString("blackUsername");
            String gameName = response.getString("gameName");
            String jsonGame = response.getString("game");
            var game = new Gson().fromJson(jsonGame, ChessGame.class);
            return new GameData(id, whiteUsername, blackUsername, gameName, game);
          } else {
            return null;
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
  }
  public void joinGame(String username, String color, int gameID) throws DataAccessException {
    checkGameExists(gameID);
    GameData game = getGame(gameID);
    if (color != null && color.equals("BLACK") && game.blackUsername() != null) {
      throw new DataAccessException("Error", 403);
    } else if (color != null && color.equals("WHITE") && game.whiteUsername() != null) {
      throw new DataAccessException("Error", 403);
    }
    ChessGame chessGame = game.game();

  }

  public Collection<GameData> listGames() throws DataAccessException {
    ArrayList<GameData> result = new ArrayList<>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT game FROM game";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        try (var response = preparedStatement.executeQuery()) {
          while (response.next()) {
            var jsonGame = response.getString("game");
            GameData game = new Gson().fromJson(jsonGame, GameData.class);
            result.add(game);
          }
          return result;
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
  }

  private final String[] createStatements = {
          """
    CREATE TABLE IF NOT EXISTS game (
      gameID int NOT NULL AUTO_INCREMENT,
      whiteUsername varchar(256) DEFAULT NULL,
      blackUsername varchar(256) DEFAULT NULL,
      gameName varchar(256) NOT NULL,
      game varchar(256) NOT NULL,
      PRIMARY KEY (gameID)
      ) 
    """
  };
  private void configureDatabase() throws DataAccessException {
    ConfigureDatabase.configureDatabase(createStatements);
  }
}
