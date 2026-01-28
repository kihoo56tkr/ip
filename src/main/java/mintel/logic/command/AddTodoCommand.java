package mintel.logic.command;

import mintel.model.task.Todo;
import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;
import mintel.exception.EmptyDescriptionException;
import java.io.IOException;

public class AddTodoCommand extends Command {
    private String input;

    public AddTodoCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws MintelException, java.io.IOException {
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

        ui.showMessage("Got it. I've added this task:\n  " + todo +
                "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    @Override
    public boolean isExit() {
        return false;
    }
}