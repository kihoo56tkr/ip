package mintel.logic.command;

import mintel.exception.EmptyDescriptionException;
import mintel.exception.MintelException;
import mintel.model.tasklist.TaskList;
import mintel.storage.Storage;
import mintel.ui.Ui;

/**
 * ViewScheduleCommand displays all deadlines and events scheduled on a specific date.
 */
public class ViewScheduleCommand extends Command {
    private final String date;

    /**
     * Constructs a ViewScheduleCommand with the specified date.
     *
     * @param input The full user input (e.g., "view 2026-03-15")
     * @throws EmptyDescriptionException If no date is provided
     */
    public ViewScheduleCommand(String input) throws EmptyDescriptionException {
        assert input != null : "Input cannot be null";
        assert input.startsWith("view") : "Command should start with 'view'";

        String[] parts = input.trim().split("\\s+", 2);

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new EmptyDescriptionException("view (date required)");
        }

        this.date = parts[1].trim();
        assert !this.date.isEmpty() : "Date should not be empty";
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws MintelException {
        assert tasks != null : "TaskList cannot be null";
        assert date != null : "Date must be set";

        return tasks.getScheduleByDate(date);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
