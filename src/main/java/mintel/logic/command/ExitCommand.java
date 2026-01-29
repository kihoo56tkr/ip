package mintel.logic.command;

import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.EmptyDescriptionException;
import mintel.exception.MintelException;

import java.io.IOException;

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
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}