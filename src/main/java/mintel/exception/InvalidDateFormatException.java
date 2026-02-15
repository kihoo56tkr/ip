package mintel.exception;

/**
 * Exception thrown when a date string cannot be parsed into a valid date.
 * Used by Deadline and Event classes for date validation.
 */
public class InvalidDateFormatException extends MintelException {

    /**
     * Constructs an InvalidDateFormatException with a custom error message.
     *
     * @param message The detail message explaining the date format error.
     */
    public InvalidDateFormatException(String message) {
        super("Date format is invalid! Format example: 2026-02-26 ₍^◞ ˕ ◟^₎⟆");
    }
}
