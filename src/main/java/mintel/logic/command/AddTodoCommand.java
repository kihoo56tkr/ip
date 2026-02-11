package mintel.logic.command;

import mintel.model.task.Todo;
import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;
import mintel.exception.EmptyDescriptionException;

import java.io.IOException;

/**
 * AddTodoCommand in the Mintel application.
 */
public class AddTodoCommand extends Command {
    private String input;

    /**
     * Constructs a AddTodoCommand command with the given description.
     *
     * @param input The command given by the user.
     */
    public AddTodoCommand(String input) {
        this.input = input;
    }

    /**
     * Executes the command with the given task list, UI, and storage by adding a new Todo Task to the task list.
     *
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws MintelException If there's an error during command execution.
     * @throws IOException If there's an error saving tasks to file.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws MintelException, java.io.IOException {
        if (input.length() <= 5) {
            throw new EmptyDescriptionException("todo");
        }

        String description = input.substring(5).trim().stripLeading();

        if (description.isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }

        Todo todo = new Todo(description);
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