package mintel.logic.command;

import mintel.model.tasklist.TaskList;
import mintel.ui.Ui;
import mintel.storage.Storage;
import mintel.exception.EmptyDescriptionException;
import mintel.exception.MintelException;
import java.io.IOException;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage(tasks.getListString());
    }

    @Override
    public boolean isExit() {
        return false;
    }
}