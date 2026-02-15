package mintel.logic.command;

import java.io.IOException;

import mintel.MainWindow;
import mintel.exception.EmptyDescriptionException;
import mintel.exception.MintelException;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;

/**
 * MarkCommand in the Mintel application.
 */
public class MarkCommand extends Command {
    private final String[] inputList;
    private final boolean isMarkAsCompleted;

    /**
     * Constructs a MarkCommand command with the given description.
     *
     * @param inputList         List of strings which contains the command given by the user but split by " ".
     * @param isMarkAsCompleted Status of the task.
     */
    public MarkCommand(String[] inputList, boolean isMarkAsCompleted) {
        assert inputList != null : "Input list cannot be null";
        assert inputList.length > 0 : "Input list should contain command";
        assert inputList[0] != null : "Command word should not be null";
        assert inputList[0].equals("mark") || inputList[0].equals("unmark")
                : "MarkCommand should only handle mark/unmark: " + inputList[0];

        this.inputList = inputList;
        this.isMarkAsCompleted = isMarkAsCompleted;

        assert this.inputList == inputList : "Input list not stored correctly";
        assert this.isMarkAsCompleted == isMarkAsCompleted : "Status flag not stored correctly";
    }

    /**
     * Executes the command with the given task list, UI, and storage by marking/unmarking the
     * target Task with the index in the input.
     *
     * @param tasks   The task list to operate on.
     * @param mainWindow      The user interface for displaying messages.
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
        assert this.inputList != null : "Input list should be initialized";

        if (this.inputList.length <= 1) {
            throw new EmptyDescriptionException(isMarkAsCompleted ? "mark" : "unmark");
        }

        try {
            int index = Integer.parseInt(inputList[1]) - 1;
            tasks.markTask(index, isMarkAsCompleted);
            storage.saveTasks(tasks.getAllTasks());

            String message = isMarkAsCompleted
                    ? "Nice! I've marked this task as done:\n  "
                    : "OK, I've marked this task as not done yet:\n  ";

            String result = message + tasks.get(index);

            assert result != null : "Result message should not be null";
            assert !result.trim().isEmpty() : "Result message should not be empty";
            assert result.contains("\n") : "Result should contain newline";

            return result;
        } catch (NumberFormatException e) {
            throw new MintelException("Please provide a valid task number! ₍^◞ ˕ ◟^₎⟆");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
