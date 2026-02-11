// File: src/test/java/mintel/logic/parser/ParserTest.java
package mintel.logic.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mintel.exception.InvalidCommandException;
import mintel.exception.MintelException;
import mintel.logic.command.AddTodoCommand;
import mintel.logic.command.Command;
import mintel.logic.command.ExitCommand;
import mintel.logic.command.ListCommand;

public class ParserTest {

    @Test
    public void testParseExitCommand() throws MintelException {
        Command command = Parser.parse("bye");
        assertTrue(command instanceof ExitCommand);
        assertTrue(command.isExit());
    }

    @Test
    public void testParseListCommand() throws MintelException {
        Command command = Parser.parse("list");
        assertTrue(command instanceof ListCommand);
        assertFalse(command.isExit());
    }

    @Test
    public void testParseTodoCommand() throws MintelException {
        Command command = Parser.parse("todo Read book");
        assertTrue(command instanceof AddTodoCommand);
        assertFalse(command.isExit());
    }

    @Test
    public void testParseInvalidCommand() {
        assertThrows(InvalidCommandException.class, () -> {
            Parser.parse("invalid command");
        });

        assertThrows(InvalidCommandException.class, () -> {
            Parser.parse("");
        });
    }

    @Test
    public void testParseMarkCommand() throws MintelException {
        Command command = Parser.parse("mark 1");
        assertNotNull(command);
        assertFalse(command.isExit());
    }

    @Test
    public void testParseUnmarkCommand() throws MintelException {
        Command command = Parser.parse("unmark 1");
        assertNotNull(command);
        assertFalse(command.isExit());
    }

    @Test
    public void testParseDeleteCommand() throws MintelException {
        Command command = Parser.parse("delete 1");
        assertNotNull(command);
        assertFalse(command.isExit());
    }
}
