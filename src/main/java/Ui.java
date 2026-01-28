import java.util.Scanner;

public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
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

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public void close() {
        scanner.close();
    }
}