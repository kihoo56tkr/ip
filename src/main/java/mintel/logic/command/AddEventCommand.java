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

    /**
     * Constructs a AddEventCommand command with the given description.
     *
     * @param input The command given by the user.
     */
    public AddEventCommand(String input) {
        assert input != null : "Event command input cannot be null";
        assert input.startsWith("event") : "AddEventCommand should start with 'event': " + input;

        this.input = input;
        assert this.input.equals(input) : "Input not stored correctly";
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
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "Ui cannot be null";
        assert storage != null : "Storage cannot be null";
        assert this.input != null : "Input should be initialized";

        if (this.input.length() <= 6) {
            throw new EmptyDescriptionException("event");
        }

        String remaining = this.input.substring(6).trim();
        String[] commandParts = remaining.split("/from", 2);

        if (commandParts.length < 2) {
            throw new MissingParameterException("/from");
        }

        String name = commandParts[0].trim();
        commandParts = commandParts[1].split("/to", 2);

        if (commandParts.length < 2) {
            throw new MissingParameterException("/to");
        }

        String from = commandParts[0].trim();
        String to = commandParts[1].trim();

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

        assert event != null : "Event creation failed";
        assert event.getName().equals(name) : "Event name not set correctly";
        assert !event.getIsDone() : "New event should not be marked done";

        tasks.add(event);
        assert tasks.get(tasks.size() - 1) == event : "Event not at end of list";

        storage.saveTasks(tasks.getAllTasks());

        return "Got it. I've added this task:\n  " + event +
                "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    @Override
    public boolean isExit() {
        return false;
    }
}