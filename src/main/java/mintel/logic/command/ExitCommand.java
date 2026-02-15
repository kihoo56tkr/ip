package mintel.logic.command;

import mintel.MainWindow;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;

/**
 * ExitCommand in the Mintel application.
 */
public class ExitCommand extends Command {

    /**
     * Exit the program.
     *
     * @param tasks   The task list to operate on.
     * @param mainWindow      The window interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     */
    @Override
    public String execute(TaskList tasks, MainWindow mainWindow, Storage storage) {
        assert tasks != null : "TaskList cannot be null";
        assert mainWindow != null : "MainWindow cannot be null";
        assert storage != null : "Storage cannot be null";

        return "Meow Meow~ Bye! See you again soon!"
                + "\n(The window will close automatically after 3 seconds)";
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
