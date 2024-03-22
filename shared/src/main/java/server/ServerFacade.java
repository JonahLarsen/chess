package server;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class ServerFacade {

  private final String serverURL;

  public ServerFacade(String url) {
    serverURL = url;
  }

  public void joinGame(int gameID, String playerColor, AuthData user) {

  }

  public GameData createGame(AuthData user, GameData game) {
    return null;
  }

  public Collection<GameData> listGames(AuthData user) {
    return null;
  }

  public void logout(AuthData user) {

  }

  public AuthData login(UserData user) {
    return null;
  }

  public void clear() {

  }

  public AuthData register(UserData user) {
    return null;
  }
}
