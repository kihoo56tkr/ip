package mintel.ui;

import java.util.Scanner;
import java.util.NoSuchElementException;

public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                return "bye";
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            return "bye";
        }
    }

    public void showWelcome(String logo) {
        System.out.println("Hello from\n" + logo);
        System.out.println("____________________________________________________________\n" +
                "Hello! I'm MIntel\n" +
                "____________________________________________________________");
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}