package mintel;

import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.model.tasklist.TaskList;
import mintel.logic.parser.Parser;
import mintel.logic.command.Command;
import mintel.exception.MintelException;

/**
 * Main class for the Mintel chatbot application.
 * Mintel is a task management chatbot that helps users track todos, deadlines, and events.
 * It supports saving tasks to file and loading them on startup.
 */
public class Mintel {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private boolean isExit;


    /**
     * Constructs a Mintel chatbot instance with the specified file path for data storage.
     *
     * @param filePath The path to the file where tasks will be saved/loaded.
     */
    public Mintel(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.isExit = false;
        loadInitialTasks();
    }

    private void loadInitialTasks() {
        String logo = "  /\\_/\\  \n" +
                " ( o.o ) \n" +
                "  > ^ <  \n" +
                " MINTEL";
        ui.showWelcome(logo);

        ui.showMessage("Doing some prechecks...");
        if (storage.fileExists()) {
            ui.showMessage("Found list_of_tasks.txt in your computer!");
            ui.showMessage("Checking file...");

            if (storage.isFileFormatCorrect()) {
                ui.showMessage("Uploaded file to the system!");
                try {
                    tasks = new TaskList(storage.loadTasks());
                } catch (MintelException e) {
                    ui.showMessage("Error loading tasks: " + e.getMessage());
                    tasks = new TaskList();
                }
            } else {
                handleInvalidFileFormat();
            }
        } else {
            ui.showMessage("list_of_tasks.txt not found!");
            tasks = new TaskList();
        }

        ui.showLine();

        if (!isExit) {
            ui.showMessage("What can I do for you?");
            ui.showLine();
        }
    }

    private void handleInvalidFileFormat() {
        ui.showMessage("File is not in correct format!");
        ui.showMessage("Do you want to overwrite file? If yes, enter \"y\". If no, enter \"n\".");

        String input = ui.readCommand();
        if (input.equals("y")) {
            ui.showMessage("Got it! File will be overwrite once a valid task is given!");
            tasks = new TaskList();
        } else if (input.equals("n")) {
            isExit = true;
            ui.showMessage("Please change the file to the correct format or rename the file before trying again!");
        } else {
            isExit = true;
            ui.showMessage("That is an invalid command! Please change the file to the correct format or rename the file before trying again!");
        }
    }

    /**
     * Runs the Mintel chatbot, handling user commands until the exit command is given.
     * This is the main loop of the application.
     */
    public void run() {
        while (!isExit) {
            try {
                String input = ui.readCommand();
                ui.showLine();
                Command command = Parser.parse(input);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (MintelException | java.io.IOException e) {
                ui.showMessage(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        ui.close();
    }

    /**
     * Generates a response to user input for GUI integration.
     * This method processes a single command and returns the response as a String.
     *
     * @param input The user's command input
     * @return Mintel's response to the command
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parse(input);
            String response = command.execute(tasks, ui, storage);
            isExit = command.isExit();

            if (isExit) {
                return response + "\n(The window will close automatically after 3 seconds)";
            }
            return response;
        } catch (MintelException | java.io.IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    public boolean isExit() {
        return this.isExit;
    }

    /**
     * The entry point of the Mintel application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Mintel("./data/list_of_task.txt").run();
    }
}