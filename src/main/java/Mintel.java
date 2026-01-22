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
        boolean isExit = false;

        while(!isExit) {
            String input = scanner.nextLine();
            String[] inputList = input.split(" " );
            System.out.println("____________________________________________________________" );
            try {
                if (input.equals("list" )) {
                    handleList();
                } else if (inputList[0].equals("mark" )) {
                    handleMark(inputList);
                } else if (inputList[0].equals("unmark" )) {
                    handleUnmark(inputList);
                } else if (input.startsWith("todo" )) {
                    handleTodo(input);
                } else if (input.startsWith("event" )) {
                    handleEvent(input);
                } else if (input.startsWith("deadline" )) {
                    handleDeadline(input);
                } else if (input.equals("bye" )) {
                    System.out.println("Bye. Hope to see you again soon!" );
                    isExit = true;
                } else if (input.startsWith("delete")) {
                    handleDelete(inputList);
                } else {
                    throw new InvalidCommandException();
                }
            } catch (MintelException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("____________________________________________________________" );
        }

        scanner.close();
    }

    private static void handleList() {
        if(listOfInput.isEmpty()) {
            System.out.println("Your list is empty!");
            return;
        }

        System.out.println("Here are the tasks in your list:");
        for(int i = 0; i < listOfInput.size(); i++) {
            Task currTask = listOfInput.get(i);
            System.out.println((i + 1) + "." + currTask.toString());
        }
    }

    private static void handleDelete(String[] inputList) throws OutOfRangeException {
        if(Integer.parseInt(inputList[1]) > taskCount) {
            throw new OutOfRangeException();
        }

        Task deleteTask = listOfInput.remove(Integer.parseInt(inputList[1]) - 1);
        taskCount--;
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + deleteTask);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void handleMark(String[] inputList) throws OutOfRangeException {
        if(Integer.parseInt(inputList[1]) > taskCount) {
            throw new OutOfRangeException();
        }
        System.out.println("Nice! I've marked this task as done:");
        Task currTask = listOfInput.get(Integer.parseInt(inputList[1]) - 1);
        currTask.markAsDone();
        System.out.println(currTask.toString());
    }

    private static void handleUnmark(String[] inputList) throws OutOfRangeException {
        if(Integer.parseInt(inputList[1]) > taskCount) {
            throw new OutOfRangeException();
        }
        System.out.println("OK, I've marked this task as not done yet:");
        Task currTask = listOfInput.get(Integer.parseInt(inputList[1]) - 1);
        currTask.unmarkAsDone();
        System.out.println(currTask.toString());
    }

    private static void handleTodo(String input) throws EmptyDescriptionException {
        if(input.length() <= 5) {
            throw new EmptyDescriptionException("todo");
        }

        input = input.substring(5).trim();
        input = input.stripLeading();

        if(input.isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }

        Todo todo = new Todo(input);
        listOfInput.add(todo);
        taskCount++;

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + todo);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void handleEvent(String input) throws MissingParameterException, EmptyDescriptionException {
        if(input.length() <= 6) {
            throw new EmptyDescriptionException("event");
        }
        input = input.substring(6).trim();
        String[] fromParts = input.split("/from", 2);

        if(fromParts.length < 2) {
            throw new MissingParameterException("/from");
        }

        String name = fromParts[0].trim();
        String[] toParts = fromParts[1].split("/to", 2);

        if(toParts.length < 2) {
            throw new MissingParameterException("/to");
        }

        String from = toParts[0];
        String to = toParts[1];

        if(name.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }

        if(from.isEmpty()) {
            throw new MissingParameterException("start time (after /from)");
        }

        if(to.isEmpty()) {
            throw new MissingParameterException("end time (after /to)");
        }
        Event event = new Event(name, from, to);
        listOfInput.add(event);
        taskCount++;

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + event);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void handleDeadline(String input) throws EmptyDescriptionException, MissingParameterException {
        if(input.length() <= 9) {
            throw new EmptyDescriptionException("deadline");
        }

        input = input.substring(9).trim();
        String[] byParts = input.split("/by", 2);

        if(byParts.length < 2) {
            throw new MissingParameterException("/by");
        }

        String name = byParts[0].trim();
        String by = byParts[1].trim();

        if(name.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }

        if(by.isEmpty()) {
            throw new EmptyDescriptionException("deadline time (after \\by)");
        }

        Deadline deadline = new Deadline(name, by);
        listOfInput.add(deadline);
        taskCount++;

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + deadline);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
