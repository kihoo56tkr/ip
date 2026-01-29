package mintel.logic.parser;

import mintel.logic.command.Command;
import mintel.logic.command.ExitCommand;
import mintel.logic.command.FindCommand;
import mintel.logic.command.ListCommand;
import mintel.logic.command.AddTodoCommand;
import mintel.logic.command.AddDeadlineCommand;
import mintel.logic.command.AddEventCommand;
import mintel.logic.command.MarkCommand;
import mintel.logic.command.DeleteCommand;
import mintel.exception.MintelException;
import mintel.exception.InvalidCommandException;

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
        } else if (input.startsWith("find")) {
            return new FindCommand(inputList, input);
        } else {
            throw new InvalidCommandException();
        }
    }
}