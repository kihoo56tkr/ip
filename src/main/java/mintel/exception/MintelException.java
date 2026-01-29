package mintel.exception;

/**
 * Base exception class for all Mintel-specific exceptions.
 * Provides a consistent way to handle errors in the Mintel application.
 */
public class MintelException extends Exception {

    /**
     * Constructs a MintelException with the specified error message.
     *
     * @param message The detail message explaining the error.
     */
    public MintelException(String message) {
        super(message);
    }
}
