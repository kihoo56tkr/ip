package mintel.model.task;

import mintel.exception.MintelException;

/**
 * Abstract base class representing a task in the Mintel application.
 * A task has a description and can be marked as done or not done.
 * Subclasses implement specific task types (Todo, Deadline, Event).
 */
public abstract class Task {
    protected String name;
    protected boolean isDone;

    private static final int TODO_EXPECTED_PARTS = 3;
    private static final int DEADLINE_EXPECTED_PARTS = 4;
    private static final int EVENT_EXPECTED_PARTS = 5;

    /**
     * Constructs a Task with the given description.
     * The task is initially not done.
     *
     * @param name The description of the task.
     */
    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Returns the status icon of the task.
     * 'X' indicates done, space indicates not done.
     *
     * @return The status icon as a string.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the status icon of the task.
     * '1' indicates done, '0'' indicates not done.
     *
     * @return The status icon as a string.
     */
    public String getStatusIconFile() {
        return (isDone ? "1" : "0");
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void unmarkAsDone() {
        this.isDone = false;
    }

    /**
     * Returns a string representation of the task for display.
     *
     * @return Formatted string representation of the task.
     */
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.name;
    }

    /**
     * Returns a string representation of the task for file storage.
     *
     * @return Formatted string suitable for saving to file.
     */
    public abstract String toStringFile();

    /**
     * Creates a Task object from a file string representation.
     *
     * @param fileString The string representation from file.
     * @return A Task object corresponding to the file string.
     * @throws MintelException If the file string format is invalid.
     */
    public static Task fromFileString(String fileString) throws MintelException {
        String[] parts = fileString.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length < TODO_EXPECTED_PARTS) {
            throw new MintelException("Invalid task format in file: " + fileString);
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;

        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < DEADLINE_EXPECTED_PARTS) {
                throw new MintelException("Invalid deadline format: " + fileString);
            }
            task = new Deadline(description, parts[3]);
            break;
        case "E":
            if (parts.length < EVENT_EXPECTED_PARTS) {
                throw new MintelException("Invalid event format: " + fileString);
            }
            task = new Event(description, parts[3].substring(5), parts[4].substring(3));
            break;
        default:
            throw new MintelException("Unknown task type in file: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}
