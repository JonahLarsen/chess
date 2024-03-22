package clientTests;

import dataAccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(String.format("http://localhost:" + port));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setUp() throws ResponseException {
        facade.clear();
    }


    @Test
    public void testJoinGamePositive() throws ResponseException {
        UserData user = new UserData("jonah", "jonah", "jonah");
        AuthData auth = facade.register(user);
        GameData game = facade.createGame(auth, new GameData(0, null, null, "myGame", null));
        Assertions.assertDoesNotThrow(()->facade.joinGame(game.gameID(), "BLACK", auth));
    }

    @Test
    public void testJoinGameNegative() throws ResponseException {
        UserData user = new UserData("jonah", "jonah", "jonah");
        AuthData auth = facade.register(user);
        GameData game = facade.createGame(auth, new GameData(0, null, null, "myGame", null));
        Assertions.assertThrows(ResponseException.class, ()->facade.joinGame(game.gameID() + 5, "BLACK", auth));
    }

    @Test
    public void testCreateGamePositive() throws ResponseException {
        UserData user = new UserData("jonah", "jonah", "jonah");
        AuthData auth = facade.register(user);
        GameData game = facade.createGame(auth, new GameData(0, null, null, "myGame", null));
        Assertions.assertNotNull(game);
    }

    @Test
    public void testCreateGameNegative() throws ResponseException {
        AuthData unaddedAuth = new AuthData("woohoo", "jonah");
        Assertions.assertThrows(ResponseException.class, ()-> facade.createGame(unaddedAuth, new GameData(0, null, null, "myGame", null)));
    }

    @Test
    public void testListGamesPositive() throws ResponseException {
        UserData user = new UserData("jonah", "jonah", "jonah");
        AuthData auth = facade.register(user);
        facade.createGame(auth, new GameData(0, null, null, "myGame", null));
        facade.createGame(auth, new GameData(0, null, null, "myGame2", null));
        Assertions.assertEquals(2, facade.listGames(auth).size());
    }

    @Test
    public void testListGamesNegative() throws ResponseException {
        UserData user = new UserData("jonah", "jonah", "jonah");
        AuthData auth = facade.register(user);
        facade.createGame(auth, new GameData(0, null, null, "myGame", null));
        facade.createGame(auth, new GameData(0, null, null, "myGame2", null));
        AuthData unaddedAuth = new AuthData("woohoo", "jonah");
        Assertions.assertThrows(ResponseException.class, ()->facade.listGames(unaddedAuth));
    }

    @Test
    public void testLogoutPositive() throws ResponseException {
        UserData user = new UserData("jonah", "jonah", "jonah");
        AuthData newUser = facade.register(user);
        Assertions.assertDoesNotThrow(()-> facade.logout(newUser));
    }

    @Test
    public void testLogoutNegative() throws ResponseException {
        AuthData user = new AuthData("jonah", "jonah");
        Assertions.assertThrows(ResponseException.class,()-> facade.logout(user));
    }

    @Test
    public void testLoginPositive() throws ResponseException {
        UserData user = new UserData("jonah", "jonah", "jonah");
        AuthData newUser = facade.register(user);
        facade.logout(newUser);
        Assertions.assertDoesNotThrow(()-> facade.login(user));

    }

    @Test
    public void testLoginNegative() throws ResponseException {
        UserData user = new UserData("jonah", "jonah", "jonah");
        Assertions.assertThrows(ResponseException.class, ()->facade.login(user) );
    }

    @Test
    public void testRegisterPositive() throws ResponseException {
        UserData user = new UserData("jonahisplayer1", "password", "player1@email.com");
        var authData = facade.register(user);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void testRegisterNegative() throws ResponseException {
        UserData user = new UserData(null, "password", "email");
        Assertions.assertThrows(ResponseException.class, ()->facade.register(user));
    }

}
