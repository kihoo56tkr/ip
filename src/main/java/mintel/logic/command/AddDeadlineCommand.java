package mintel.logic.command;

import mintel.exception.EmptyDescriptionException;
import mintel.exception.MissingParameterException;
import mintel.model.task.Deadline;
import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;

import java.io.IOException;

public class AddDeadlineCommand extends Command {
    private String input;

    public AddDeadlineCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws MintelException, java.io.IOException {

        if (input.length() <= 9) {
            throw new EmptyDescriptionException("deadline");
        }

        String remaining = input.substring(9).trim();
        String[] byParts = remaining.split("/by", 2);

        if (byParts.length < 2) {
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
        tasks.add(deadline);
        storage.saveTasks(tasks.getAllTasks());

        ui.showMessage("Got it. I've added this task:\n  " + deadline +
                "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    @Override
    public boolean isExit() {
        return false;
    }
}