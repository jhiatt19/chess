package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import ui.UserClient;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static UserClient facade = new UserClient("http://localhost:8080");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear() throws ResponseException {
        facade.eval("register ho jo jo");
        assertDoesNotThrow(() -> facade.clear());
    }
    @Test
    public void createUser() throws ResponseException {
        var authData = facade.eval("register testPlayer1 password p1@email.com");
        assertEquals("You successfully registered as testplayer1.", authData);
    }

    @Test
    public void createUserBad() throws ResponseException {
        facade.eval("register testPlayer1 password p1@email.com");
        var x = facade.eval("register testPlayer1 password p1@email.com");
        assertNotEquals("You successfully registered as testplayer1.",x);
    }

    @Test
    public void logoutUser() throws ResponseException {
        facade.eval("register testPlayer1 password p1@email.com");
        var res = facade.eval("logout");
        assertEquals("Goodbye!",res);
        var res1 = facade.eval("logout");
        assertEquals("You must sign in", res1);
    }

    @Test
    public void logoutUserExtra() throws ResponseException {
        facade.eval("register jo jo jo");
        var res = facade.eval("logout please");
        assertEquals("Goodbye!", res);
    }

    @Test
    public void loginUser() throws ResponseException {
        facade.eval("register ho jo jo");
        var res = facade.eval("login ho jo");
        assertEquals("Welcome ho", res);
    }

    @Test
    public void loginUserBadCredentials() throws ResponseException {
        var badUser = facade.eval("login jo jo");
        assertEquals("Error: unauthorized", badUser);
        var badPassword = facade.eval("login ho ho");
        assertEquals("Error: unauthorized", badPassword);
        var noCredentials = facade.eval("login");
        assertEquals("Expected: <username> <password>", noCredentials);
    }

    @Test
    public void createGame() throws ResponseException {
        facade.eval("register jo jo jo");
        var res = facade.eval("createGame testGame");
        assertEquals("Created game: testgame, with ID number: 1", res);
    }

    @Test
    public void createGameNoName() throws ResponseException {
        facade.eval("register jo jo jo");
        var res = facade.eval("createGame");
        assertEquals("Expected: <gameName>", res);
    }

    @Test
    public void listGames() throws ResponseException {
        facade.eval("register jo jo jo");
        facade.eval("createGame testGame");
        var res = facade.eval("listGames");
        assertEquals("\nID: 1 white: null black: null gameName: testgame\n",res);
    }

    @Test
    public void joinGame() throws ResponseException {
        facade.eval("register jo jo jo");
        facade.eval("createGame testGame");
        assertDoesNotThrow(() -> facade.eval("playGame 1 WHITE"));
    }

    @Test
    public void stealColor() throws ResponseException {
        facade.eval("register jo jo jo");
        facade.eval("createGame testGame");
        facade.eval("playGame 1 WHITE");
        var res = facade.eval("playGame 1 WHITE");
        assertEquals("Error: already taken: Color already taken",res);
    }

    @Test
    public void observe() throws ResponseException {
        facade.eval("register jo jo jo");
        facade.eval("createGame testGame");
        assertDoesNotThrow(() -> facade.eval("observe 1"));
    }

    @Test
    public void observeNotGameIDProvided() throws ResponseException {
        facade.eval("register jo jo jo");
        assertEquals("Not a valid gameID",facade.eval("watchGame 1"));
        facade.eval("createGame testGame");
        var res = facade.eval("watchGame");
        assertEquals("Expected: <GameID>",res);
    }

    @Test
    public void help() throws ResponseException {
        assertDoesNotThrow(() -> facade.eval("help"));
    }

    @Test
    public void helpExtra() throws ResponseException {
        var exp = facade.eval("help");
        assertEquals(exp,facade.eval("help please"));
    }

    @Test
    public void clearAll() throws ResponseException{
        facade.eval("register jo jo jo");
        facade.eval("logout");
        facade.eval("register ho ho ho");
        assertDoesNotThrow(() -> facade.eval("clear"));
        assertEquals("Error: unauthorized", facade.eval("login jo jo"));
    }

    @Test
    public void anotherTest() throws ResponseException {
        assertEquals("You successfully registered as jo.",facade.eval("register jo jo jo"));
        assertEquals("Created game: testgame, with ID number: 1",facade.eval("createGame testgame"));
        assertDoesNotThrow(() -> facade.eval("playGame 1 WHITE"));
        assertEquals("Error: already taken: Color already taken",facade.eval("playGame 1 WHITE"));
        assertDoesNotThrow(() -> facade.eval("playGame 1 BLACK"));
        assertEquals("\nID: 1 white: jo black: jo gameName: testgame\n",facade.eval("listgames"));
    }

}
