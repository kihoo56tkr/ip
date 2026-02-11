package mintel.logic.command;

import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;
import mintel.ui.Ui;

/**
 * ListCommand in the Mintel application.
 */
public class ListCommand extends Command {

    /**
     * Prints out each task in the task list.
     *
     * @param tasks   The task list to operate on.
     * @param ui      The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";

        String tasksListString = tasks.getListString();
        assert tasksListString != null : "List result should not be null";

        return tasksListString;
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
