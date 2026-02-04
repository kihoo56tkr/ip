package mintel.logic.command;

import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;

/**
 * ExitCommand in the Mintel application.
 */
public class ExitCommand extends Command {

    /**
     * Exit the program.
     *
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return "Bye. Hope to see you again soon!";
    }

    @Override
    public boolean isExit() {
        return true;
    }
}