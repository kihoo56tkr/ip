package mintel.model.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TodoTest {

    @Test
    public void testTodoCreation() {
        Todo todo = new Todo("Read book");
        assertEquals("[T][ ] Read book", todo.toString());
    }

    @Test
    public void testTodoMarkAsDone() {
        Todo todo = new Todo("Read book");
        todo.markAsDone();
        assertEquals("[T][X] Read book", todo.toString());
    }

    @Test
    public void testTodoUnmarkAsDone() {
        Todo todo = new Todo("Read book");
        todo.markAsDone();
        todo.unmarkAsDone();
        assertEquals("[T][ ] Read book", todo.toString());
    }

    @Test
    public void testTodoFileFormat() {
        Todo todo = new Todo("Read book");
        assertEquals("T | 0 | Read book", todo.toStringFile());

        todo.markAsDone();
        assertEquals("T | 1 | Read book", todo.toStringFile());
    }
}