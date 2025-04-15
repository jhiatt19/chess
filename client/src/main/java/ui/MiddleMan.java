package ui;

import javax.management.Notification;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class MiddleMan {
    private final UserClient client;
    private String appState = "";

    public MiddleMan(String serverUrl) {
        client = new UserClient(serverUrl);
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
                } else if (result.isEmpty()) {
                    printPrompt();
                }
                else {
                    System.out.print(result);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.println();
        if (client.getState().equals(State.SIGNEDIN)) {
            if (appState.isEmpty()) {
                appState = "[Chess]";
                System.out.print(appState + " >>> ");
            }
        }
        else if (client.getState().equals(State.GAMEPLAY)) {
            appState = "[Chess Game]";
            System.out.print(appState + " >>> ");
        }
        else {
            System.out.print("[LOGGED_OUT]" + " >>> ");
        }
    }

    public void notify(Notification notification){
        System.out.println(notification.getMessage());
        printPrompt();
    }
}
