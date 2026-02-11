package mintel.logic.command;

import mintel.exception.MissingParameterException;
import mintel.model.task.Event;
import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;
import mintel.exception.EmptyDescriptionException;

import java.io.IOException;

/**
 * AddEventCommand in the Mintel application.
 */
public class AddEventCommand extends Command {
    private String input;
    private static final int EVENT_MIN_LENGTH = 6;
    private static final int MIN_PARTS = 2;

    /**
     * Constructs a AddEventCommand command with the given description.
     *
     * @param input The command given by the user.
     */
    public AddEventCommand(String input) {
        this.input = input;
    }

    /**
     * Executes the command with the given task list, UI, and storage by adding a new Event Task to the task list.
     *
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws MintelException If there's an error during command execution.
     * @throws IOException If there's an error saving tasks to file.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage)
            throws MintelException, java.io.IOException {

        if (input.length() <= EVENT_MIN_LENGTH) {
            throw new EmptyDescriptionException("event");
        }

        String remaining = input.substring(EVENT_MIN_LENGTH).trim();
        String[] commandParts = remaining.split("/from", MIN_PARTS);

        if (commandParts.length < MIN_PARTS) {
            throw new MissingParameterException("/from");
        }

        String name = commandParts[0].trim();
        String[] commandPartsRemaining = commandParts[1].split("/to", 2);

        if (commandPartsRemaining.length < MIN_PARTS) {
            throw new MissingParameterException("/to");
        }

        String from = commandPartsRemaining[0].trim();
        String to = commandPartsRemaining[1].trim();

        if (name.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }

        if (from.isEmpty()) {
            throw new MissingParameterException("start time (after /from)");
        }

        if (to.isEmpty()) {
            throw new MissingParameterException("end time (after /to)");
        }

        Event event = new Event(name, from, to);
        tasks.add(event);
        storage.saveTasks(tasks.getAllTasks());

        return "Got it. I've added this task:\n  " + event +
                "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    @Override
    public boolean isExit() {
        return false;
    }
}