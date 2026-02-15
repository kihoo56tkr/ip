package mintel.exception;

/**
 * Exception thrown when the index given is out of range.
 * Used by functions Mark, Unmark and Delete.
 */
public class OutOfRangeException extends MintelException {

    /**
     * Constructs an OutOfRangeException with an error message.
     */
    public OutOfRangeException() {
        super("That is more than the number of tasks in the list! Meow...");
    }
}
