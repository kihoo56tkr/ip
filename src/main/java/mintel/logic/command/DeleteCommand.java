package mintel.logic.command;

import java.io.IOException;

import mintel.MainWindow;
import mintel.exception.EmptyDescriptionException;
import mintel.exception.MintelException;
import mintel.model.task.Task;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;

/**
 * DeleteCommand in the Mintel application.
 */
public class DeleteCommand extends Command {
    private final String[] inputList;

    /**
     * Constructs a DeleteCommand command with the given description.
     *
     * @param inputList List of strings which contains the command given by the user but split by " ".
     */
    public DeleteCommand(String[] inputList) {
        assert inputList != null : "Input list cannot be null";
        assert inputList.length > 0 : "Input list should contain command";
        assert inputList[0] != null : "Command word should not be null";
        assert inputList[0].equals("delete") : "DeleteCommand should only handle delete: ";

        this.inputList = inputList;

        assert this.inputList == inputList : "Input list not stored correctly";
    }

    /**
     * Executes the command with the given task list, UI, and storage by deleting the target
     * Task with the index in the input.
     *
     * @param tasks   The task list to operate on.
     * @param mainWindow      The window interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws MintelException If there's an error during command execution.
     * @throws IOException     If there's an error saving tasks to file.
     */
    @Override
    public String execute(TaskList tasks, MainWindow mainWindow, Storage storage)
            throws MintelException, java.io.IOException {
        assert tasks != null : "TaskList cannot be null";
        assert mainWindow != null : "MainWindow cannot be null";
        assert storage != null : "Storage cannot be null";

        if (this.inputList.length <= 1) {
            throw new EmptyDescriptionException("delete");
        }

        try {
            int index = Integer.parseInt(this.inputList[1]) - 1;

            Task deletedTask = tasks.remove(index);
            assert deletedTask != null : "Deleted Task cannot be null";

            storage.saveTasks(tasks.getAllTasks());

            return "MEOWRiffic! I've removed this task:\n  " + deletedTask
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
        } catch (NumberFormatException e) {
            throw new MintelException("Please provide a valid task number! Meow...");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
