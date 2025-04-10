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

}
