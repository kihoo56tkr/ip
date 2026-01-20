public class EmptyDescriptionException extends MintelException {
    public EmptyDescriptionException(String taskType) {
        super("The description of a " + taskType + " cannot be empty! ; _ ;");
    }
}
