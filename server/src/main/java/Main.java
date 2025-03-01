import chess.*;
import dataaccess.*;
import server.Server;
import services.AuthService;
import services.GameService;
import services.UserService;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess server.Server: " + piece);

        try {
            var port = 8080;

            UserDAO userAccess = new UserMemoryAccess();
            var userService = new UserService(userAccess);

            GameDAO gameAccess = new GameMemoryAccess();
            var gameService = new GameService(gameAccess);

            AuthDAO authAccess = new AuthMemoryAccess();
            var authService = new AuthService(authAccess);

            Server server = new Server(authService, gameService, userService).run(port);
            System.out.printf("Server started on port %d with Memory Access Data%n", port);
        }
        catch (Throwable ex) {
            System.out.printf("Unable to start server: %s$%n",ex.getMessage());
        }
    }
}