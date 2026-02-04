package mintel.logic.command;

import mintel.model.task.Task;
import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;
import mintel.exception.EmptyDescriptionException;

import java.io.IOException;

/**
 * DeleteCommand in the Mintel application.
 */
public class DeleteCommand extends Command {
    private String[] inputList;

    /**
     * Constructs a DeleteCommand command with the given description.
     *
     * @param inputList List of strings which contains the command given by the user but split by " ".
     */
    public DeleteCommand(String[] inputList) {
        this.inputList = inputList;
    }

    /**
     * Executes the command with the given task list, UI, and storage by deleting the target Task with the index in the input.
     *
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws MintelException If there's an error during command execution.
     * @throws IOException If there's an error saving tasks to file.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage)
            throws MintelException, java.io.IOException {

        if (inputList.length <= 1) {
            throw new EmptyDescriptionException("delete");
        }

        try {
            int index = Integer.parseInt(inputList[1]) - 1;
            Task deletedTask = tasks.remove(index);
            storage.saveTasks(tasks.getAllTasks());

            return "Noted. I've removed this task:\n  " + deletedTask +
                    "\nNow you have " + tasks.size() + " tasks in the list.";
        } catch (NumberFormatException e) {
            throw new MintelException("Please provide a valid task number!");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}