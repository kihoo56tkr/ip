public class Deadline extends Task {
    protected String by;

    public Deadline(String name, String by) {
        super(name);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by + ")";
    }

    @Override
    public String toStringFile() {
        return "D | " + super.getStatusIconFile() + " | " + super.name + " | " + this.by;
    }
}
