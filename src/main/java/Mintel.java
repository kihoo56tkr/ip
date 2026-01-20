import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Mintel {
    public static void main(String[] args) {
        String logo = "  /\\_/\\  \n" +
                " ( o.o ) \n" +
                "  > ^ <  \n" +
                " MINTEL";
        System.out.println("Hello from\n" + logo);
        System.out.println("____________________________________________________________\n" +
                        "Hello! I'm MIntel\n" +
                        "What can I do for you?\n" +
                        "____________________________________________________________");

        List<Task> listOfInput = new ArrayList<Task>(100);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while(!input.equals("bye")) {
            System.out.println("____________________________________________________________");
            if(input.equals("list")) {
                for(int i = 0; i < listOfInput.size(); i++) {
                    Task currTask = listOfInput.get(i);
                    System.out.println((i + 1) + ". " + currTask.getName());
                }
            } else {
                Task newTask = new Task(input);
                listOfInput.add(newTask);
                System.out.println(newTask.getName());
            }
            System.out.println("____________________________________________________________");
            input = scanner.nextLine();
        }

        System.out.println("____________________________________________________________\n" +
                "Bye. Hope to see you again soon!\n" +
                "____________________________________________________________");

        scanner.close();
    }
}
