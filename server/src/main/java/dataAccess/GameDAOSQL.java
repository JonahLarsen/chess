package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


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
      try (var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, gameName);
        preparedStatement.setString(2, jsonGame);
        preparedStatement.executeUpdate();

        var result = preparedStatement.getGeneratedKeys();
        if (result.next()) {
          return result.getInt(1);
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error", 500);
    }
    return 0;
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

  public void updateGame(String whiteUsername, String blackUsername,
                         ChessGame chess, int gameID) throws DataAccessException {
    var jsonGame = new Gson().toJson(chess);
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "UPDATE game SET whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        preparedStatement.setString(1, whiteUsername);
        preparedStatement.setString(2, blackUsername);
        preparedStatement.setString(3, jsonGame);
        preparedStatement.setInt(4, gameID);
        preparedStatement.executeUpdate();
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
    if (color != null && color.equals("BLACK")) {
      try (var conn = DatabaseManager.getConnection()) {
        var statement = "UPDATE game SET blackUsername = ? WHERE gameID = ?";
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.setString(1, username);
          preparedStatement.setInt(2, gameID);
          preparedStatement.executeUpdate();
        }
      } catch (SQLException e) {
        throw new DataAccessException("Error", 500);
      }
    } else if (color != null && color.equals("WHITE")) {
      try (var conn = DatabaseManager.getConnection()) {
        var statement = "UPDATE game SET whiteUsername = ? WHERE gameID = ?";
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.setString(1, username);
          preparedStatement.setInt(2, gameID);
          preparedStatement.executeUpdate();
        }
        game = getGame(1);
      } catch (SQLException e) {
        throw new DataAccessException("Error", 500);
      }
    }
  }

  public Collection<GameData> listGames() throws DataAccessException {
    HashSet<GameData> result = new HashSet<>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
      try (var preparedStatement = conn.prepareStatement(statement)) {
        try (var response = preparedStatement.executeQuery()) {
          while (response.next()) {
            int id = response.getInt("gameID");
            String whiteUsername = response.getString("whiteUsername");
            String blackUsername = response.getString("blackUsername");
            String gameName = response.getString("gameName");
            String jsonGame = response.getString("game");
            ChessGame game = new Gson().fromJson(jsonGame, ChessGame.class);
            result.add(new GameData(id, whiteUsername, blackUsername, gameName, game));
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
      game text NOT NULL,
      PRIMARY KEY (gameID)
      ) 
    """
  };
  private void configureDatabase() throws DataAccessException {
    ConfigureDatabase.configureDatabase(createStatements);
  }
}
