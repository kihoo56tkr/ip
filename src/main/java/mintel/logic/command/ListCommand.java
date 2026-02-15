package mintel.logic.command;

import java.io.IOException;

import mintel.MainWindow;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;

/**
 * ListCommand in the Mintel application.
 */
public class ListCommand extends Command {

    /**
     * Prints out each task in the task list.
     *
     * @param tasks   The task list to operate on.
     * @param mainWindow      The window interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     */
    @Override
    public String execute(TaskList tasks, MainWindow mainWindow, Storage storage) throws IOException {
        assert tasks != null : "TaskList cannot be null";
        assert mainWindow != null : "mainWindow cannot be null";
        assert storage != null : "Storage cannot be null";

        String tasksListString = tasks.getListString();
        assert tasksListString != null : "List result should not be null";

        storage.saveTasks(tasks.getAllTasks());

        return tasksListString;
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
