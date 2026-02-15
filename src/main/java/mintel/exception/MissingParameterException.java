package mintel.exception;

/**
 * Exception thrown when a parameter required in the Task classes are empty.
 * Used by Todo, Deadline and Event class for paramater validation.
 */
public class MissingParameterException extends MintelException {

    /**
     * Constructs an MissingParameterException with a custom error message.
     *
     * @param message The parameter missing in the command.
     */
    public MissingParameterException(String message) {
        super("You are missing the " + message + " parameter! Meow...");
    }
}
