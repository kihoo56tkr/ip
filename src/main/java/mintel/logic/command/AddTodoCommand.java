package mintel.logic.command;

import java.io.IOException;

import mintel.exception.EmptyDescriptionException;
import mintel.exception.MintelException;
import mintel.model.task.Todo;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;
import mintel.ui.Ui;

/**
 * AddTodoCommand in the Mintel application.
 */
public class AddTodoCommand extends Command {
    private final String input;
    private static final int TODO_MIN_LENGTH = 5;

    /**
     * Constructs a AddTodoCommand command with the given description.
     *
     * @param input The command given by the user.
     */
    public AddTodoCommand(String input) {
        assert input != null : "Input cannot be null";

        this.input = input;

        assert this.input == input : "Input not stored correctly";
    }

    /**
     * Executes the command with the given task list, UI, and storage by adding a new Todo Task to the task list.
     *
     * @param tasks   The task list to operate on.
     * @param ui      The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws MintelException If there's an error during command execution.
     * @throws IOException     If there's an error saving tasks to file.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws MintelException, java.io.IOException {
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";

        if (this.input.length() <= TODO_MIN_LENGTH) {
            throw new EmptyDescriptionException("todo");
        }

        assert this.input.length() > 5 : "Input length must be more than 5";

        String description = this.input.substring(TODO_MIN_LENGTH).trim().stripLeading();

        if (description.isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }

        Todo todo = new Todo(description);
        assert todo != null : "todo cannot be null";

        tasks.add(todo);
        storage.saveTasks(tasks.getAllTasks());

        return "Got it. I've added this task:\n  " + todo +
                "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
