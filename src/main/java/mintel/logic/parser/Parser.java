package mintel.logic.parser;

import mintel.exception.InvalidCommandException;
import mintel.exception.MintelException;
import mintel.logic.command.AddDeadlineCommand;
import mintel.logic.command.AddEventCommand;
import mintel.logic.command.AddTodoCommand;
import mintel.logic.command.Command;
import mintel.logic.command.DeleteCommand;
import mintel.logic.command.ExitCommand;
import mintel.logic.command.FindCommand;
import mintel.logic.command.HelpCommand;
import mintel.logic.command.ListCommand;
import mintel.logic.command.MarkCommand;
import mintel.logic.command.ViewScheduleCommand;

/**
 * Parses user input strings into Command objects.
 * Determines which command the user intends to execute.
 */
public class Parser {

    /**
     * Parses a user input string and returns the corresponding Command object.
     *
     * @param input The raw user input string.
     * @return A Command object corresponding to the input.
     * @throws InvalidCommandException If the input cannot be parsed as a valid command.
     */
    public static Command parse(String input) throws MintelException {
        String[] inputList = input.split(" ");
        assert inputList.length > 0 : "Input split resulted in empty array";
        assert inputList[0] != null : "First word of command should not be null";

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
        } else if (input.startsWith("find")) {
            return new FindCommand(inputList, input);
        } else if (input.startsWith("view")) {
            return new ViewScheduleCommand(input);
        } else if (input.equals("/help")) {
            return new HelpCommand();
        } else {
            throw new InvalidCommandException();
        }
    }
}
