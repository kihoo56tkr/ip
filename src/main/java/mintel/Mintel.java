package mintel;

import java.util.List;

import mintel.exception.MintelException;
import mintel.logic.command.Command;
import mintel.logic.parser.Parser;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;

/**
 * Main class for the Mintel chatbot application.
 * Mintel is a task management chatbot that helps users track todos, deadlines, and events.
 * It supports saving tasks to file and loading them on startup.
 */
public class Mintel {
    private final Storage storage;
    private TaskList tasks;
    private boolean isExit;
    private MainWindow mainWindow;

    /**
     * Constructs a Mintel chatbot instance with the specified file path for data storage.
     *
     * @param filePath The path to the file where tasks will be saved/loaded.
     */
    public Mintel(String filePath) {
        assert filePath != null : "File path must not be null";
        assert !filePath.trim().isEmpty() : "File path must not be empty";

        this.storage = new Storage(filePath);
        this.isExit = false;
    }

    /**
     * Sets the main window for GUI interactions.
     */
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Initializes tasks by loading from storage.
     * Should be called after mainWindow is set.
     */
    public void initializeTasks() {
        assert mainWindow != null : "MainWindow must be set before loading tasks";

        try {
            tasks = new TaskList(storage.loadTasks());

            List<String> warnings = storage.getWarnings();
            if (!warnings.isEmpty()) {
                StringBuilder warningMessage = new StringBuilder();
                for (String warning : warnings) {
                    warningMessage.append(warning).append("\n");
                }
                mainWindow.showMessage(warningMessage.toString().trim());
            }

        } catch (MintelException e) {
            mainWindow.showMessage(e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Generates a response to user input for GUI integration.
     * This method processes a single command and returns the response as a String.
     *
     * @param input The user's command input
     * @return Mintel's response to the command
     */
    public String getResponse(String input) {
        assert input != null : "Input to getResponse() cannot be null";
        assert tasks != null : "Tasks must be initialized before handling input";
        assert mainWindow != null : "MainWindow must be set before handling input";

        try {
            Command command = Parser.parse(input);
            String response = command.execute(tasks, mainWindow, storage);
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
     * The entry point of the Mintel application (CLI mode).
     * Note: This is not used when running with GUI.
     */
    public static void main(String[] args) {
    }
}