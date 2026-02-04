package mintel.logic.command;

import mintel.exception.EmptyDescriptionException;
import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;

import java.io.IOException;

/**
 * MarkCommand in the Mintel application.
 */
public class MarkCommand extends Command {
    private String[] inputList;
    private boolean isMarkAsCompleted;

    /**
     * Constructs a MarkCommand command with the given description.
     *
     * @param inputList List of strings which contains the command given by the user but split by " ".
     * @param isMarkAsCompleted Status of the task.
     */
    public MarkCommand(String[] inputList, boolean isMarkAsCompleted) {
        this.inputList = inputList;
        this.isMarkAsCompleted = isMarkAsCompleted;
    }

    /**
     * Executes the command with the given task list, UI, and storage by marking/unmarking the target Task with the index in the input.
     *
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws MintelException If there's an error during command execution.
     * @throws IOException If there's an error saving tasks to file.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws MintelException, java.io.IOException {
        if (inputList.length <= 1) {
            throw new EmptyDescriptionException(isMarkAsCompleted ? "mark" : "unmark");
        }

        try {
            int index = Integer.parseInt(inputList[1]) - 1;
            tasks.markTask(index, isMarkAsCompleted);
            storage.saveTasks(tasks.getAllTasks());

            String message = isMarkAsCompleted ?
                    "Nice! I've marked this task as done:\n  " :
                    "OK, I've marked this task as not done yet:\n  ";
            ui.showMessage(message + tasks.get(index));
        } catch (NumberFormatException e) {
            throw new MintelException("Please provide a valid task number!");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}