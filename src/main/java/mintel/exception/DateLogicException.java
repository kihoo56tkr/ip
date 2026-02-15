package mintel.exception;

/**
 * Exception thrown when the 'From' date is after the 'To' date.
 * Used by Event class for date validation.
 */
public class DateLogicException extends MintelException {

    /**
     * Constructs an DateLogicException with a custom error message.
     *
     * @param message The detail message explaining the date error.
     */
    public DateLogicException(String message) {
        super("The 'From' date should be before the 'To' date! ₍^◞ ˕ ◟^₎⟆");
    }
}
