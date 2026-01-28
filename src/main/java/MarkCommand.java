public class MarkCommand extends Command {
    private String[] inputList;
    private boolean isMark;

    public MarkCommand(String[] inputList, boolean isMark) {
        this.inputList = inputList;
        this.isMark = isMark;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws MintelException, java.io.IOException {
        if (inputList.length <= 1) {
            throw new EmptyDescriptionException(isMark ? "mark" : "unmark");
        }

        try {
            int index = Integer.parseInt(inputList[1]) - 1;
            tasks.markTask(index, isMark);
            storage.saveTasks(tasks.getAllTasks());

            String message = isMark ?
                    "Nice! I've marked this task as done:\n  " :
                    "OK, I've marked this task as not done yet:\n  ";
            ui.showMessage(message + tasks.get(index));
        } catch (NumberFormatException e) {
            throw new MintelException("Please provide a valid task number!");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}