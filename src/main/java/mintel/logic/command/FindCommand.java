package mintel.logic.command;

import mintel.model.task.Task;
import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;
import mintel.exception.EmptyDescriptionException;

public class FindCommand extends Command {
    private String[] inputList;
    private String input;

    public FindCommand(String[] inputList, String input) {
        this.inputList = inputList;
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws MintelException, java.io.IOException {
        if (inputList.length <= 1) {
            throw new EmptyDescriptionException("find");
        }

        String keyword = input.substring(5);
        String filteredTasks = tasks.getFilteredTasks(keyword);

        if(filteredTasks.isEmpty()) {
            ui.showMessage("There is no tasks that contains the keyword \'" + keyword +  "\'!");
        } else {
            ui.showMessage("Here are the matching tasks in your list:");
            ui.showMessage(filteredTasks);
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
