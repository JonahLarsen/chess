package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
  ChessGame.TeamColor playerColor;
  int gameID;
  public JoinPlayer(String authToken, ChessGame.TeamColor playerColor, int gameID) {
    super(authToken);
    this.commandType = CommandType.JOIN_PLAYER;
    this.playerColor = playerColor;
    this.gameID = gameID;
  }

  public ChessGame.TeamColor getPlayerColor() {
    return this.playerColor;
  }

  public int getGameID() {
    return this.gameID;
  }
}
