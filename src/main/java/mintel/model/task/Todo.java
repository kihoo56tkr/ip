package mintel.model.task;

import mintel.exception.MintelException;
import mintel.exception.EmptyDescriptionException;

public class Todo extends Task {

    public Todo(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toStringFile() {
        return "T | " + super.getStatusIconFile() + " | " + super.name;
    }
}


