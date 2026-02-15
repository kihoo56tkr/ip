package mintel.logic.command;

import java.io.IOException;

import mintel.MainWindow;
import mintel.exception.MintelException;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;

/**
 * Abstract base class for all commands in the Mintel application.
 * Each command knows how to execute itself and whether it causes the application to exit.
 */
public abstract class Command {

    /**
     * Executes the command with the given task list, UI, and storage.
     *
     * @param tasks   The task list to operate on.
     * @param mainWindow      The window for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws MintelException If there's an error during command execution.
     * @throws IOException     If there's an error saving tasks to file.
     */
    public abstract String execute(TaskList tasks, MainWindow mainWindow, Storage storage)
            throws MintelException, IOException;

    /**
     * Returns whether this command causes the application to exit.
     *
     * @return true if the command is an exit command, false otherwise.
     */
    public abstract boolean isExit();
}
