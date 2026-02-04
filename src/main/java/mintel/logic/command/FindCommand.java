package mintel.logic.command;

import java.io.IOException;

import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;
import mintel.exception.EmptyDescriptionException;

/**
 * FindCommand in the Mintel application.
 */
public class FindCommand extends Command {
    private String[] inputList;
    private String input;

    /**
     * Constructs a FindCommand command with the given description.
     *
     * @param inputList List of strings which contains the command given by the user but split by " ".
     * @param input String of the input given by the user.
     */
    public FindCommand(String[] inputList, String input) {
        this.inputList = inputList;
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
        if (inputList.length <= 1) {
            throw new EmptyDescriptionException("find");
        }

        String keyword = input.substring(5);
        String filteredTasks = tasks.getFilteredTasks(keyword);

        if(filteredTasks.isEmpty()) {
            return "There is no tasks that contains the keyword \'" + keyword +  "\'!";
        } else {
            return "Here are the matching tasks in your list:\n" + filteredTasks;
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
