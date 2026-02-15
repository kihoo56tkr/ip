package mintel.logic.command;

import java.io.IOException;

import mintel.MainWindow;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;

/**
 * Displays a list of all available commands and their usage.
 */
public class HelpCommand extends Command {

    private static final String HELP_MESSAGE = "<<Mintel Commands>>\n\n"
            + "Adding Tasks:\n"
            + "todo DESCRIPTION\n"
            + "e.g., todo read book\n\n"
            + "deadline DESCRIPTION /by DATE\n"
            + "e.g., deadline homework /by 2026-03-15\n"
            + "e.g., deadline project /by Mar 20 2026\n\n"
            + "event DESCRIPTION /from DATE /to DATE\n"
            + "e.g., event meeting /from 2026-03-15 /to 2026-03-16\n"
            + "e.g., event conference /from Mar 15 2026 /to Mar 18 2026\n\n"
            + "Viewing Tasks:\n"
            + "list - Show all tasks\n"
            + "find KEYWORDS - Find tasks containing keywords\n"
            + "e.g., find book read\n"
            + "view DATE - Show schedule for a specific date\n"
            + "e.g., view 2026-03-15\n\n"
            + "Managing Tasks:\n"
            + "mark INDEX - Mark task as done\n"
            + "e.g., mark 2\n"
            + "unmark INDEX - Mark task as not done\n"
            + "e.g., unmark 2\n"
            + "delete INDEX - Delete task\n"
            + "e.g., delete 3\n\n"
            + "Other Commands:\n"
            + "/help - Show this help message\n"
            + "bye - Exit Mintel\n\n"
            + "Date Formats:\n"
            + "yyyy-MM-dd (e.g., 2026-03-15)\n"
            + "MMM d yyyy (e.g., Mar 15 2026)";

    @Override
    public String execute(TaskList tasks, MainWindow mainWindow, Storage storage) throws IOException {
        assert mainWindow != null : "mainWindow cannot be null";
        storage.saveTasks(tasks.getAllTasks());
        return HELP_MESSAGE;
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
