package mintel.exception;

public class DateLogicException extends MintelException {
    public DateLogicException(String message) {
        super("The 'From' date should be before the 'To' date!");
    }
}

