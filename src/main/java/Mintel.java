import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Mintel {
    private static List<Task> listOfInput = new ArrayList<Task>(100);
    private static int taskCount = 0;

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

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while(!input.equals("bye")) {
            String[] inputList = input.split(" ");
            System.out.println("____________________________________________________________");
            if(input.equals("list")) {
                handleList();
            } else if(inputList[0].equals("mark")) {
                handleMark(inputList);
            } else if(inputList[0].equals("unmark")) {
                handleUnmark(inputList);
            } else if(input.startsWith("todo")){
                handleTodo(input);
            } else if(input.startsWith("event")) {
                handleEvent(input);
            } else if(input.startsWith("deadline")) {
                handleDeadline(input);
            }
            System.out.println("____________________________________________________________");
            input = scanner.nextLine();
        }

        System.out.println("____________________________________________________________\n" +
                "Bye. Hope to see you again soon!\n" +
                "____________________________________________________________");

        scanner.close();
    }

    private static void handleList() {
        System.out.println("Here are the tasks in your list:");
        for(int i = 0; i < listOfInput.size(); i++) {
            Task currTask = listOfInput.get(i);
            System.out.println((i + 1) + "." + currTask.toString());
        }
    }

    private static void handleMark(String[] inputList) {
        System.out.println("Nice! I've marked this task as done:");
        Task currTask = listOfInput.get(Integer.parseInt(inputList[1]) - 1);
        currTask.markAsDone();
        System.out.println(currTask.toString());
    }

    private static void handleUnmark(String[] inputList) {
        System.out.println("OK, I've marked this task as not done yet:");
        Task currTask = listOfInput.get(Integer.parseInt(inputList[1]) - 1);
        currTask.unmarkAsDone();
        System.out.println(currTask.toString());
    }

    private static void handleTodo(String input) {
        input = input.substring(5).trim();
        Todo todo = new Todo(input);
        listOfInput.add(todo);
        taskCount++;

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + todo);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void handleEvent(String input) {
        input = input.substring(6).trim();
        String[] fromParts = input.split("/from", 2);
        String name = fromParts[0].trim();
        String[] toParts = fromParts[1].split("/to", 2);
        String from = toParts[0];
        String to = toParts[1];
        Event event = new Event(name, from, to);
        listOfInput.add(event);
        taskCount++;

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + event);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void handleDeadline(String input) {
        input = input.substring(9).trim();
        String[] byParts = input.split("/by", 2);
        String name = byParts[0].trim();
        String by = byParts[1].trim();
        Deadline deadline = new Deadline(name, by);
        listOfInput.add(deadline);
        taskCount++;

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + deadline);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
