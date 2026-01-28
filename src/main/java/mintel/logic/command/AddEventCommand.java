package mintel.logic.command;

import mintel.exception.MissingParameterException;
import mintel.model.task.Event;
import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;
import mintel.exception.EmptyDescriptionException;
import java.io.IOException;

public class AddEventCommand extends Command {
    private String input;

    public AddEventCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws MintelException, java.io.IOException {

        if (input.length() <= 6) {
            throw new EmptyDescriptionException("event");
        }

        String remaining = input.substring(6).trim();
        String[] fromParts = remaining.split("/from", 2);

        if (fromParts.length < 2) {
            throw new MissingParameterException("/from");
        }

        String name = fromParts[0].trim();
        String[] toParts = fromParts[1].split("/to", 2);

        if (toParts.length < 2) {
            throw new MissingParameterException("/to");
        }

        String from = toParts[0].trim();
        String to = toParts[1].trim();

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

        ui.showMessage("Got it. I've added this task:\n  " + event +
                "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    @Override
    public boolean isExit() {
        return false;
    }
}