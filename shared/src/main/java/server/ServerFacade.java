package server;

import model.AuthData;
import model.GameData;

import java.util.Collection;

public class ServerFacade {

  private final String serverURL;

  public ServerFacade(String url) {
    serverURL = url;
  }

  public void joinGame() {

  }

  public GameData createGame() {
    return null;
  }

  public Collection<GameData> listGames() {
    return null;
  }

  public void logout() {

  }

  public AuthData login() {
    return null;
  }

  public void clear() {

  }

  public AuthData register() {
    return null;
  }
}
