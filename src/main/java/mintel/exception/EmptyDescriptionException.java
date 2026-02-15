package mintel.exception;

/**
 * Exception thrown when description of the Task classes are empty.
 * Used by Todo, Deadline and Event class for paramater validation.
 */
public class EmptyDescriptionException extends MintelException {

    /**
     * Constructs an EmptyDescriptionException with a custom error message.
     *
     * @param taskType The Task class with missing description.
     */
    public EmptyDescriptionException(String taskType) {
        super("The description of a " + taskType + " cannot be empty! ₍^◞ ˕ ◟^₎⟆");
    }
}
