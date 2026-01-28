package mintel.exception;

/**
 * Exception thrown when an invalid command is given.
 * Used by Parser class for command validation.
 */
public class InvalidCommandException extends MintelException {

    /**
     * Constructs an InvalidCommandException with an error message.
     */
    public InvalidCommandException() {
        super("That is an invalid command! ; _ ;");
    }
}
