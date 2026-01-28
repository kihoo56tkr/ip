public class InvalidDateFormatException extends MintelException {
    public InvalidDateFormatException(String message) {
        super("Date format is invalid! Format example: 2026-02-26");
    }
}
