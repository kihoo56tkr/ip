package mintel.logic.command;

import java.io.IOException;

import mintel.MainWindow;
import mintel.exception.EmptyDescriptionException;
import mintel.exception.MintelException;
import mintel.exception.MissingParameterException;
import mintel.model.task.Deadline;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;

/**
 * AddDeadlineCommand in the Mintel application.
 */
public class AddDeadlineCommand extends Command {
    private static final int DEADLINE_MIN_LENGTH = 9;
    private static final int MIN_PARTS = 2;

    private final String input;

    /**
     * Constructs a AddDeadlineCommand command with the given description.
     *
     * @param input The command given by the user.
     */
    public AddDeadlineCommand(String input) {
        this.input = input;
    }

    /**
     * Executes the command with the given task list, UI, and storage by adding a new Deadline Task to the task list.
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
        assert this.input != null : "Input should be initialized";

        if (this.input.length() <= DEADLINE_MIN_LENGTH) {
            throw new EmptyDescriptionException("deadline");
        }

        String remaining = this.input.substring(DEADLINE_MIN_LENGTH).trim();
        String[] byParts = remaining.split("/by", MIN_PARTS);

        if (byParts.length < MIN_PARTS) {
            throw new MissingParameterException("/by");
        }

        String name = byParts[0].trim();
        String by = byParts[1].trim();

        if (name.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }

        if (by.isEmpty()) {
            throw new EmptyDescriptionException("deadline time (after /by)");
        }

        Deadline deadline = new Deadline(name, by);

        assert deadline != null : "Deadline creation failed";
        assert deadline.getName().equals(name) : "Deadline name not set correctly";
        assert !deadline.getIsDone() : "New deadline should not be marked done";

        tasks.add(deadline);
        assert tasks.get(tasks.size() - 1) == deadline : "Event not at end of list";

        storage.saveTasks(tasks.getAllTasks());

        return "MEOWRiffic! I've added this task:\n  " + deadline
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Returns whether this command causes the application to exit.
     *
     * @return true if the command is an exit command, false otherwise.
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
