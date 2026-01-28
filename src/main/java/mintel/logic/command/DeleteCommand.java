package mintel.logic.command;

import mintel.model.task.Task;
import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;
import mintel.exception.EmptyDescriptionException;
import java.io.IOException;

public class DeleteCommand extends Command {
    private String[] inputList;

    public DeleteCommand(String[] inputList) {
        this.inputList = inputList;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws MintelException, java.io.IOException {

        if (inputList.length <= 1) {
            throw new EmptyDescriptionException("delete");
        }

        try {
            int index = Integer.parseInt(inputList[1]) - 1;
            Task deletedTask = tasks.remove(index);
            storage.saveTasks(tasks.getAllTasks());

            ui.showMessage("Noted. I've removed this task:\n  " + deletedTask +
                    "\nNow you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            throw new MintelException("Please provide a valid task number!");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}