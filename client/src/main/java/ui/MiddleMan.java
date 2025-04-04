package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class MiddleMan {
    private final UserClient client;

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
                } else {
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
        if (client.getState().equals(State.SIGNEDIN)) {
            System.out.print("\n" + ">>> ");
        } else {
            System.out.print("[LOGGED_OUT]" + " >>> ");
        }
    }
}
