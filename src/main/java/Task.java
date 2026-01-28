public abstract class Task {
    protected String name;
    protected boolean isDone;

    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    public String getName() {
        return this.name;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public String getStatusIconFile() {
        return (isDone ? "1" : "0");
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void unmarkAsDone() {
        this.isDone = false;
    }

    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.name;
    }

    public abstract String toStringFile();

    public static Task fromFileString(String fileString) throws MintelException {
        String[] parts = fileString.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length < 3) {
            throw new MintelException("Invalid task format in file: " + fileString);
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;

        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    throw new MintelException("Invalid deadline format: " + fileString);
                }
                task = new Deadline(description, parts[3]);
                break;
            case "E":
                if (parts.length < 5) {
                    throw new MintelException("Invalid event format: " + fileString);
                }
                task = new Event(description, parts[3].substring(5), parts[4].substring(3));
                break;
            default:
                throw new MintelException("Unknown task type in file: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}
