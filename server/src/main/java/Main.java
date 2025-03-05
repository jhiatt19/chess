import chess.*;
import dataaccess.*;
import exception.ResponseException;
import server.Server;
import services.AuthService;
import services.GameService;
import services.UserService;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess server.Server: " + piece);

        try {
            Server server = new Server();
            server.run(8080);
            //System.out.printf("Server started on port %d with Memory Access Data%n");
        }
        catch (Throwable ex) {
            System.out.printf("Unable to start server: %s$%n",ex.getMessage());
        }
    }
}