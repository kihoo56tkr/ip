public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws MintelException, java.io.IOException;
    public abstract boolean isExit();
}