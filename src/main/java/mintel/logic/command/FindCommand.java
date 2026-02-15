package mintel.logic.command;

import java.io.IOException;

import mintel.Main;
import mintel.MainWindow;
import mintel.exception.EmptyDescriptionException;
import mintel.exception.MintelException;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;

/**
 * FindCommand in the Mintel application.
 */
public class FindCommand extends Command {
    private final String[] inputList;
    private final String input;

    /**
     * Constructs a FindCommand command with the given description.
     *
     * @param inputList List of strings which contains the command given by the user but split by " ".
     * @param input     String of the input given by the user.
     */
    public FindCommand(String[] inputList, String input) {
        assert inputList != null : "Input list cannot be null";
        assert inputList.length > 0 : "Input list should contain command";
        assert inputList[0] != null : "Command word should not be null";
        assert inputList[0].equals("find") : "FindCommand should only handle find: ";

        this.inputList = inputList;
        this.input = input;

        assert this.inputList == inputList : "Input list not stored correctly";
        assert this.input == input : "Input not stored correctly";
    }

    /**
     * Executes the command with the given task list, UI, and storage by adding a new Todo Task to the task list.
     *
     * @param tasks   The task list to operate on.
     * @param mainWindow      The window interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws MintelException If there's an error during command execution.
     * @throws IOException     If there's an error saving tasks to file.
     */
    @Override
    public String execute(TaskList tasks, MainWindow mainWindow, Storage storage) throws MintelException, java.io.IOException {
        assert tasks != null : "TaskList cannot be null";
        assert mainWindow != null : "MainWindow cannot be null";
        assert storage != null : "Storage cannot be null";
        assert this.inputList != null : "Input list should be initialized";

        if (this.inputList.length <= 1) {
            throw new EmptyDescriptionException("find");
        }

        String keywordsString = this.input.substring(5).trim();
        assert keywordsString.length() >= 1 : "KeywordsString cannot be empty";

        String[] keywords = keywordsString.split("\\s+");
        assert keywords.length >= 1 : "Keywords cannot be empty";

        String filteredTasks = tasks.getFilteredTasks(keywords);

        if (filteredTasks.isEmpty()) {
            return "Meow~ There is no tasks that contains the keyword '" + keywordsString + "'!";
        } else {
            return "Meow~ Here are the matching tasks in your list:\n" + filteredTasks;
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
