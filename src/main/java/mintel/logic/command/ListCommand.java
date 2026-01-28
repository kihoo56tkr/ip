package mintel.logic.command;

import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.EmptyDescriptionException;
import mintel.exception.MintelException;
import java.io.IOException;

/**
 * ListCommand in the Mintel application.
 */
public class ListCommand extends Command {

    /**
     * Prints out each task in the task list.
     *
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage(tasks.getListString());
    }

    @Override
    public boolean isExit() {
        return false;
    }
}