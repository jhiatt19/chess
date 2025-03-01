import chess.*;
import server.Server;
import services.GameService;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess server.Server: " + piece);

        try {
            var port = 8080;

            var gameService = new GameService()

            Server server = new Server();
            server.run(port);
        }
        catch (Throwable ex) {
            System.out.printf("Unable to start server: %s$%n",ex.getMessage());
        }
    }
}