package mintel.exception;

/**
 * Thrown when a file operation fails (missing, permission denied, etc.)
 */
public class FileOperationException extends MintelException {

    /**
     * Constructs an FileOperationException with a custom error message.
     *
     * @param message The detail message explaining the file operation error.
     */
    public FileOperationException(String message) {
        super(message + "Meow...");
    }
}
