import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Mintel {
    private static List<Task> listOfInput = new ArrayList<Task>(100);
    private static int taskCount = 0;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        boolean isExit = false;

        String logo = "  /\\_/\\  \n" +
                " ( o.o ) \n" +
                "  > ^ <  \n" +
                " MINTEL";
        System.out.println("Hello from\n" + logo);
        System.out.println("____________________________________________________________\n" +
                "Hello! I'm MIntel\n" +
                "____________________________________________________________");

        System.out.println("Doing some prechecks...");
        if(fileInSystem()) {
            System.out.println("Found list_of_tasks.txt in your computer!");
            System.out.println("Checking file...");
            if(fileCorrectFormat()) {
                System.out.println("Uploaded file to the system!");
                readListOfInputFile();
            } else {
                System.out.println("File is not in correct format!");
                System.out.println("Do you want to overwrite file? If yes, enter \"y\". If no, enter \"n\".");
                String input = scanner.nextLine();
                if(input.equals("y")) {
                    System.out.println("Got it! File will be overwrite once a valid task is given!");
                    readListOfInputFile();
                } else if (input.equals("n")) {
                    isExit = true;
                    System.out.println("Please change the file to the correct format or rename the file before trying again!");
                } else {
                    isExit = true;
                    System.out.println("That is an invalid command! Please change the file to the correct format or rename the file before trying again!");
                }
            }
        } else {
            System.out.println("list_of_tasks.txt not found!");
        }

        System.out.println("____________________________________________________________" );

        if(!isExit) {
            System.out.println("What can I do for you?");
            System.out.println("____________________________________________________________" );
        }

        while(!isExit) {
            String input = scanner.nextLine();
            String[] inputList = input.split(" " );
            System.out.println("____________________________________________________________" );
            try {
                if (input.equals("list")) {
                    handleList();
                } else if (inputList[0].equals("mark")) {
                    handleMark(inputList);
                } else if (inputList[0].equals("unmark")) {
                    handleUnmark(inputList);
                } else if (input.startsWith("todo")) {
                    handleTodo(input);
                } else if (input.startsWith("event")) {
                    handleEvent(input);
                } else if (input.startsWith("deadline")) {
                    handleDeadline(input);
                } else if (input.equals("bye" )) {
                    System.out.println("Bye. Hope to see you again soon!" );
                    isExit = true;
                } else if (input.startsWith("delete")) {
                    handleDelete(inputList);
                } else {
                    throw new InvalidCommandException();
                }
            } catch (MintelException | IOException e) {
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

    private static void handleDelete(String[] inputList) throws OutOfRangeException, IOException, EmptyDescriptionException {
        if(inputList.length <= 1) {
            throw new EmptyDescriptionException("delete");
        }

        if(Integer.parseInt(inputList[1]) > taskCount) {
            throw new OutOfRangeException();
        }

        Task deleteTask = listOfInput.remove(Integer.parseInt(inputList[1]) - 1);
        taskCount--;
        writeToFile(listOfInput);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + deleteTask);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void handleMark(String[] inputList) throws OutOfRangeException, IOException, EmptyDescriptionException {
        if(inputList.length <= 1) {
            throw new EmptyDescriptionException("mark");
        }

        if(Integer.parseInt(inputList[1]) > taskCount) {
            throw new OutOfRangeException();
        }
        System.out.println("Nice! I've marked this task as done:");
        Task currTask = listOfInput.get(Integer.parseInt(inputList[1]) - 1);
        currTask.markAsDone();
        writeToFile(listOfInput);
        System.out.println(currTask.toString());
    }

    private static void handleUnmark(String[] inputList) throws OutOfRangeException, IOException, EmptyDescriptionException {
        if(inputList.length <= 1) {
            throw new EmptyDescriptionException("unmark");
        }

        if(Integer.parseInt(inputList[1]) > taskCount) {
            throw new OutOfRangeException();
        }
        System.out.println("OK, I've marked this task as not done yet:");
        Task currTask = listOfInput.get(Integer.parseInt(inputList[1]) - 1);
        currTask.unmarkAsDone();
        writeToFile(listOfInput);
        System.out.println(currTask.toString());
    }

    private static void handleTodo(String input) throws EmptyDescriptionException, IOException {
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
        writeToFile(listOfInput);

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + todo);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void handleEvent(String input) throws MissingParameterException, EmptyDescriptionException, IOException {
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
        writeToFile(listOfInput);

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + event);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void handleDeadline(String input) throws EmptyDescriptionException, MissingParameterException, IOException {
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
        writeToFile(listOfInput);

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + deadline);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void writeToFile(List<Task> listOfInput) throws IOException {
        File dataDir = new File("./data");
        if(!dataDir.exists()) {
            dataDir.mkdirs();
        }

        FileWriter fw = new FileWriter("./data/list_of_task.txt");
        for(int i = 0; i < listOfInput.size(); i++) {
            Task currTask = listOfInput.get(i);
            fw.write(currTask.toStringFile() + "\n");
        }
        fw.close();
    }

    private static boolean fileInSystem() {
        File file = new File("./data/list_of_task.txt");
        return file.exists() && file.isFile();
    }

    private static boolean fileCorrectFormat() throws FileNotFoundException {
        File taskFile = new File("./data/list_of_task.txt");

        try (Scanner scanner = new Scanner(taskFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", -1);
                int pipeCount = parts.length - 1;

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                if (parts[0].equals("T")) {
                    if (pipeCount != 2 || parts.length != 3) {
                        System.out.println("Invalid Todo format: " + line);
                        return false;
                    }
                } else if (parts[0].equals("D")) {
                    if (pipeCount != 3 || parts.length != 4) {
                        System.out.println("Invalid Deadline format: " + line);
                        return false;
                    }
                } else if (parts[0].equals("E")) {
                    if (pipeCount != 4 || parts.length != 5) {
                        System.out.println("Invalid Event format: " + line);
                        return false;
                    }
                } else {
                    System.out.println("Unknown task type: " + parts[0]);
                    return false;
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Save file not found.");
            return true;
        }
    }

    private static void readListOfInputFile() {
        File taskFile = new File("./data/list_of_task.txt");

        try (Scanner scanner = new Scanner(taskFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    listOfInput.add(parseValidatedLine(line));
                }
            }
        } catch (FileNotFoundException e) {}
    }

    private static Task parseValidatedLine(String line) {
        String[] parts = line.split("\\|");
        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();
        Task task;

        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                task = new Deadline(description, parts[3].trim());
                break;
            case "E":
                task = new Event(description, parts[3].trim(), parts[4].trim());
                break;
            default:
                throw new IllegalStateException("Validated file has invalid type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }

        taskCount += 1;

        return task;
    }
}
