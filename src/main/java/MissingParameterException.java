public class MissingParameterException extends MintelException {
    public MissingParameterException(String message) {
        super("You are missing the " + message + " parameter! ; _ ;");
    }
}
