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
            String[] inputList = input.split(" ");
            System.out.println("____________________________________________________________");
            if(input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for(int i = 0; i < listOfInput.size(); i++) {
                    Task currTask = listOfInput.get(i);
                    System.out.println((i + 1) + "." + currTask.getStatusLine());
                }
            } else if(inputList[0].equals("mark")) {
                System.out.println("Nice! I've marked this task as done:");
                Task currTask = listOfInput.get(Integer.parseInt(inputList[1]) - 1);
                currTask.markAsDone();
                System.out.println(currTask.getStatusLine());
            } else if(inputList[0].equals("unmark")) {
                System.out.println("OK, I've marked this task as not done yet:");
                Task currTask = listOfInput.get(Integer.parseInt(inputList[1]) - 1);
                currTask.unmarkAsDone();
                System.out.println(currTask.getStatusLine());
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
