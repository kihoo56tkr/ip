package mintel.exception;

/**
 * Exception thrown when there is a duplicate Task.
 * Used by TaskList class for task validation.
 */
public class DataValidationException extends MintelException {

    /**
     * Constructs an DataValidationException with a custom error message.
     *
     * @param message The detail message explaining the data error.
     */
    public DataValidationException(String message) {
        super("A task with identical details already exists! ₍^◞ ˕ ◟^₎⟆");
    }
}