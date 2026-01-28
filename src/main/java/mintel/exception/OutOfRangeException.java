package mintel.exception;

public class OutOfRangeException extends MintelException {
    public OutOfRangeException() {
        super("That is more than the number of tasks in the list! ; _ ;");
    }
}
