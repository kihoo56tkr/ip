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

    /**
     * Constructs a Task with the given description.
     * The task is initially not done.
     *
     * @param name The description of the task.
     */
    public Task(String name) {
        assert name != null : "Task description cannot be null";
        assert !name.trim().isEmpty() : "Task description cannot be empty or whitespace";

        this.name = name;
        this.isDone = false;

        assert this.name.equals(name.trim()) : "Name not trimmed correctly";
        assert !this.isDone : "New task should not be marked done";
        assert this.name.length() > 0 : "Trimmed name should not be empty";
    }

    /**
     * @return description of the task.
     */
    public String getName() {
        assert this.name != null : "Task name should not be null";
        assert !this.name.isEmpty() : "Task name should not be empty";

        return this.name;
    }

    /**
     * Returns the status icon of the task.
     * 'X' indicates done, space indicates not done.
     *
     * @return The status icon as a string.
     */
    public String getStatusIcon() {
        String icon = (isDone ? "X" : " ");
        assert icon.length() == 1 : "Status icon should be single character";
        assert icon.equals("X") || icon.equals(" ") : "Status icon should be 'X' or space";

        return icon;
    }

    /**
     * Returns the status icon of the task.
     * '1' indicates done, '0'' indicates not done.
     *
     * @return The status icon as a string.
     */
    public String getStatusIconFile() {
        String icon = (isDone ? "1" : "0");
        assert icon.length() == 1 : "File status icon should be single character";
        assert icon.equals("1") || icon.equals("0") : "File status icon should be '1' or '0'";

        return icon;
    }

    /**
     * @return whether the task is mark as done.
     */
    public boolean getIsDone() {
        return this.isDone;
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
        assert this.name != null : "Task name should not be null in toString()";

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
        assert fileString != null : "File string cannot be null";
        assert !fileString.trim().isEmpty() : "File string cannot be empty";
        assert fileString.contains("|") : "File string should contain pipe delimiter: " + fileString;

        String[] parts = fileString.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length < 3) {
            throw new MintelException("Invalid task format in file: " + fileString);
        }

        assert parts.length >= 3 : "File string should have at least 3 parts: " + fileString;

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        assert type != null : "Task type should not be null";
        assert isDone == (parts[1].equals("1")) : "Status parsing incorrect";
        assert description != null : "Task description should not be null";

        Task task;

        switch (type) {
        case "T":
            assert parts.length == 3 : "Todo should have exactly 3 parts";
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new MintelException("Invalid deadline format: " + fileString);
            }
            assert parts.length == 4 : "Deadline should have exactly 4 parts";
            task = new Deadline(description, parts[3]);
            break;
        case "E":
            if (parts.length < 5) {
                throw new MintelException("Invalid event format: " + fileString);
            }
            assert parts.length == 5 : "Event should have exactly 5 parts";
            task = new Event(description, parts[3].substring(5), parts[4].substring(3));
            break;
        default:
            throw new MintelException("Unknown task type in file: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }

        assert task.getIsDone() == isDone : "Task done status mismatch";
        assert task.getName() != null : "Created task has null name";
        assert !task.getName().isEmpty() : "Created task has empty name";

        return task;
    }
}
