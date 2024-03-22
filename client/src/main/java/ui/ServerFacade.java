package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.GamesListWrapper;
import server.JoinGameObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

public class ServerFacade {

  private final String serverURL;

  public ServerFacade(String url) {
    serverURL = url;
  }

  public void joinGame(int gameID, String playerColor, AuthData user) throws ResponseException {
    String path = "/game";
    JoinGameObject game = new JoinGameObject(gameID, playerColor);
    this.makeRequest("PUT", path, game, user.authToken(), null);
  }

  public GameData createGame(AuthData user, GameData game) throws ResponseException {
    String path = "/game";
    return this.makeRequest("POST", path, game, user.authToken(), GameData.class);
  }

  public Collection<GameData> listGames(AuthData user) throws ResponseException {
    String path = "/game";
    GamesListWrapper wrappedGames = this.makeRequest("GET", path, null, user.authToken(), GamesListWrapper.class);
    return wrappedGames.games();
  }

  public void logout(AuthData user) throws ResponseException {
    String path = "/session";
    this.makeRequest("DELETE", path, null, user.authToken(), null);
  }

  public AuthData login(UserData user) throws ResponseException{
    String path = "/session";
    return this.makeRequest("POST", path, user, null, AuthData.class);
  }

  public void clear() throws ResponseException {
    String path = "/db";
    this.makeRequest("DELETE", path, null, null, null);
  }

  public AuthData register(UserData user) throws ResponseException {
    String path = "/user";
    return this.makeRequest("POST", path, user, null, AuthData.class);
  }

  private <T> T makeRequest(String method, String path, Object request, String header, Class<T> responseClass) throws ResponseException {
    try {
      URL url = (new URI(serverURL + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

      http.addRequestProperty("authorization", header);

      writeBody(request, http);
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }


  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ResponseException(status, "failure: " + status);
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }


  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
}
