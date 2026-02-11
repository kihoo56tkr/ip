package mintel.model.task;

import mintel.exception.MintelException;
import mintel.exception.EmptyDescriptionException;

/**
 * Represents a todo task without any date/time attached.
 * A todo is the simplest type of task with just a description.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the given description.
     *
     * @param name The description of the todo.
     * @throws EmptyDescriptionException If the description is empty.
     */
    public Todo(String name) {
        super(name);
        assert name != null : "Todo description cannot be null";
    }

    /**
     * Returns a string representation of the todo for display.
     * Format: [T][status] description
     *
     * @return Formatted string representation.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns a string representation of the todo for file storage.
     * Format: T | status | description
     *
     * @return Formatted string for file storage.
     */
    @Override
    public String toStringFile() {
        String result = "T | " + this.getStatusIconFile() + " | " + super.name;
        assert result.startsWith("T | ") : "Todo file format incorrect";
        assert result.contains(" | ") : "Todo file format missing delimiter";
        return result;
    }
}


