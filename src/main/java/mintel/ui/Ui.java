package mintel.ui;

import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 * Handles all user interface interactions for the Mintel chatbot.
 * This class is responsible for displaying messages to the user,
 * reading user input, and managing the console interface.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Constructs a new Ui instance with a Scanner for reading user input.
     * The Scanner reads from System.in (standard input).
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a command from the user via standard input.
     * Handles cases where no input is available (e.g., when running in non-interactive mode).
     *
     * @return The user's input as a string, or "bye" if no input is available.
     * @throws NoSuchElementException If the scanner is closed or input is exhausted.
     */
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

    /**
     * Displays the welcome message and Mintel logo to the user.
     * This is called once at the start of the application.
     *
     * @param logo The ASCII art logo of Mintel to display.
     */
    public void showWelcome(String logo) {
        System.out.println("Hello from\n" + logo);
        System.out.println("____________________________________________________________\n" +
                "Hello! I'm MIntel\n" +
                "____________________________________________________________");
    }

    /**
     * Displays a horizontal line to separate different sections of output.
     * Used for formatting the console output for better readability.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays a message to the user.
     * This is the primary method for outputting information to the console.
     *
     * @param message The message to display to the user.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays the goodbye message when the user exits the application.
     * This is called before the application terminates.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Closes the scanner and releases any associated system resources.
     * This should be called when the Ui is no longer needed,
     * typically at the end of the application.
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}