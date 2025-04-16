package ui;

import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class MiddleMan implements NotificationHandler {
    private final UserClient client;
    private String appState = "";

    public MiddleMan(String serverUrl) {
        client = new UserClient(serverUrl,this);
    }

    public void run() {
        System.out.println("Welcome! Please sign in to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                if (result.equals("quit")) {
                    System.out.print("Goodbye");
                }
                else {
                    System.out.print(result);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt() {
        System.out.println();
        if (client.getState().equals(State.SIGNEDIN)) {
            appState = "[Chess]";
            System.out.print(appState + " >>> ");
        }
        else if (client.getState().equals(State.GAMEPLAY)) {
            appState = "[Chess Game]";
            System.out.print(appState + " >>> ");
        }
        else {
            System.out.print("[LOGGED_OUT]" + " >>> ");
        }
    }

    public void notify(ServerMessage notification){
        System.out.println(notification);
        printPrompt();
    }
}
