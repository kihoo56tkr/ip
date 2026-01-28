package mintel.logic.command;

import mintel.exception.EmptyDescriptionException;
import mintel.exception.MissingParameterException;
import mintel.model.task.Deadline;
import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.MintelException;
import java.io.IOException;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws MintelException, java.io.IOException;
    public abstract boolean isExit();
}
