public class Parser {
    public static Command parse(String input) throws MintelException {
        String[] inputList = input.split(" ");

        if (input.equals("list")) {
            return new ListCommand();
        } else if (inputList[0].equals("mark")) {
            return new MarkCommand(inputList, true);
        } else if (inputList[0].equals("unmark")) {
            return new MarkCommand(inputList, false);
        } else if (input.startsWith("todo")) {
            return new AddTodoCommand(input);
        } else if (input.startsWith("event")) {
            return new AddEventCommand(input);
        } else if (input.startsWith("deadline")) {
            return new AddDeadlineCommand(input);
        } else if (input.equals("bye")) {
            return new ExitCommand();
        } else if (input.startsWith("delete")) {
            return new DeleteCommand(inputList);
        } else {
            throw new InvalidCommandException();
        }
    }
}