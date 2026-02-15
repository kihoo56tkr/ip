package mintel.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import mintel.exception.FileOperationException;
import mintel.exception.MintelException;
import mintel.model.task.Task;

/**
 * Handles loading and saving of tasks to/from a file.
 * Manages file existence checks and format validation.
 */
public class Storage {
    private static final String TASK_TYPE_TODO = "T";
    private static final int TODO_EXPECTED_PARTS = 3;
    private static final int TODO_EXPECTED_PIPE_COUNT = 2;

    private static final String TASK_TYPE_DEADLINE = "D";
    private static final int DEADLINE_EXPECTED_PARTS = 4;
    private static final int DEADLINE_EXPECTED_PIPE_COUNT = 3;

    private static final String TASK_TYPE_EVENT = "E";
    private static final int EVENT_EXPECTED_PARTS = 5;
    private static final int EVENT_EXPECTED_PIPE_COUNT = 4;

    private final String filePath;
    private List<String> warnings;

    /**
     * Constructs a Storage instance with the specified file path.
     *
     * @param filePath The path to the task storage file.
     */
    public Storage(String filePath) {
        assert filePath != null : "Storage file path cannot be null";
        assert !filePath.trim().isEmpty() : "Storage file path cannot be empty";
        assert filePath.endsWith(".txt") : "Storage file should be a .txt file: " + filePath;

        this.filePath = filePath;
        this.warnings = new ArrayList<>();
        assert this.filePath.equals(filePath) : "File path not stored correctly";
    }

    /**
     * Gets any warnings from the last load operation.
     */
    public List<String> getWarnings() {
        return warnings;
    }

    /**
     * Loads tasks from the storage file with error recovery.
     * If a line has invalid format, it's skipped and warning is stored.
     *
     * @return A list of valid tasks loaded from the file.
     * @throws FileOperationException If critical file access issues occur.
     */
    public ArrayList<Task> loadTasks() throws MintelException {
        assert filePath != null : "File path must be initialized";

        ArrayList<Task> tasks = new ArrayList<>();
        File taskFile = new File(filePath);
        warnings.clear();

        if (!fileExists(taskFile)) {
            return tasks;
        }

        processFileLines(taskFile, tasks);

        addSummaryWarning(tasks.size());
        return tasks;
    }

    /**
     * Checks if file exists.
     */
    private boolean fileExists(File taskFile) {
        return taskFile.exists();
    }

    /**
     * Processes each line in the file.
     */
    private void processFileLines(File taskFile, ArrayList<Task> tasks) throws MintelException {
        int lineNumber = 0;

        try (Scanner scanner = new Scanner(taskFile)) {
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                processLine(line, lineNumber, tasks);
            }
        } catch (FileNotFoundException e) {
            throw new MintelException(e.getMessage() + " Meow...");
        }
    }

    /**
     * Processes a single line from the file.
     */
    private void processLine(String line, int lineNumber, ArrayList<Task> tasks) {
        String[] parts = splitAndTrimLine(line);

        if (hasBlankFields(parts, line, lineNumber)) {
            return;
        }

        if (!isValidTaskFormat(parts, line, lineNumber)) {
            return;
        }

        parseAndAddTask(line, lineNumber, tasks);
    }

    /**
     * Splits line by pipe delimiter and trims each part.
     */
    private String[] splitAndTrimLine(String line) {
        String[] parts = line.split("\\|", -1);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    /**
     * Checks for blank fields in the line.
     */
    private boolean hasBlankFields(String[] parts, String line, int lineNumber) {
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) {
                addBlankFieldWarning(line, lineNumber, i + 1);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds warning for blank field.
     */
    private void addBlankFieldWarning(String line, int lineNumber, int position) {
        warnings.add("Meow~ Skipping invalid line " + lineNumber + ": " + line);
        warnings.add("  └─ Empty field at position " + position);
    }

    /**
     * Validates task format based on task type.
     */
    private boolean isValidTaskFormat(String[] parts, String line, int lineNumber) {
        String taskType = parts[0];
        int pipeCount = parts.length - 1;

        if (taskType.equals(TASK_TYPE_TODO)) {
            return validateTodoFormat(parts, pipeCount, line, lineNumber);
        } else if (taskType.equals(TASK_TYPE_DEADLINE)) {
            return validateDeadlineFormat(parts, pipeCount, line, lineNumber);
        } else if (taskType.equals(TASK_TYPE_EVENT)) {
            return validateEventFormat(parts, pipeCount, line, lineNumber);
        } else {
            addUnknownTypeWarning(line, lineNumber, taskType);
            return false;
        }
    }

    /**
     * Validates Todo task format.
     */
    private boolean validateTodoFormat(String[] parts, int pipeCount, String line, int lineNumber) {
        if (pipeCount != TODO_EXPECTED_PIPE_COUNT || parts.length != TODO_EXPECTED_PARTS) {
            addFormatWarning(line, lineNumber, "Todo", TODO_EXPECTED_PARTS);
            return false;
        }
        return true;
    }

    /**
     * Validates Deadline task format.
     */
    private boolean validateDeadlineFormat(String[] parts, int pipeCount, String line, int lineNumber) {
        if (pipeCount != DEADLINE_EXPECTED_PIPE_COUNT || parts.length != DEADLINE_EXPECTED_PARTS) {
            addFormatWarning(line, lineNumber, "Deadline", DEADLINE_EXPECTED_PARTS);
            return false;
        }
        return true;
    }

    /**
     * Validates Event task format.
     */
    private boolean validateEventFormat(String[] parts, int pipeCount, String line, int lineNumber) {
        if (pipeCount != EVENT_EXPECTED_PIPE_COUNT || parts.length != EVENT_EXPECTED_PARTS) {
            addFormatWarning(line, lineNumber, "Event", EVENT_EXPECTED_PARTS);
            return false;
        }
        return true;
    }

    /**
     * Adds warning for incorrect format.
     */
    private void addFormatWarning(String line, int lineNumber, String taskType, int expectedParts) {
        warnings.add("Meow~ Skipping invalid line " + lineNumber + ": " + line);
        warnings.add("  └─ " + taskType + " should have " + expectedParts + " fields");
    }

    /**
     * Adds warning for unknown task type.
     */
    private void addUnknownTypeWarning(String line, int lineNumber, String taskType) {
        warnings.add("Meow~ Skipping invalid line " + lineNumber + ": " + line);
        warnings.add("  └─ Unknown task type: " + taskType);
    }

    /**
     * Parses line and adds task to list.
     */
    private void parseAndAddTask(String line, int lineNumber, ArrayList<Task> tasks) {
        try {
            Task task = Task.fromFileString(line);
            tasks.add(task);
        } catch (MintelException e) {
            addParseWarning(line, lineNumber, e.getMessage());
        }
    }

    /**
     * Adds warning for parsing error.
     */
    private void addParseWarning(String line, int lineNumber, String errorMessage) {
        warnings.add("Meow~ Skipping invalid line " + lineNumber + ": " + line);
        warnings.add("  └─ " + errorMessage);
    }

    /**
     * Adds summary warning at the end.
     */
    private void addSummaryWarning(int taskCount) {
        if (!warnings.isEmpty()) {
            warnings.add(0, "Meow~ Found " + (warnings.size() / 2) + " issue(s) in file:");
            warnings.add("Loaded " + taskCount + " valid tasks.");
            warnings.add("Invalid Tasks will be removed once a valid command is given!");
        }
    }

    /**
     * Saves tasks to the storage file.
     *
     * @param tasks The list of tasks to save.
     * @throws IOException If there's an error writing to the file.
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        assert tasks != null : "Task list to save cannot be null";
        assert filePath != null : "File path must be initialized";

        File dataDir = new File("./data");
        assert dataDir != null : "Data directory object creation failed";

        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            assert created : "Failed to create data directory: ./data";
            assert dataDir.exists() : "Data directory still doesn't exist after creation";
            assert dataDir.isDirectory() : "./data is not a directory";
        }

        assert dataDir.canWrite() : "Cannot write to data directory";

        FileWriter fw = new FileWriter(filePath);
        assert fw != null : "FileWriter creation failed";

        for (Task task : tasks) {
            String line = task.toStringFile();
            assert line != null : "Task.toStringFile() returned null";
            assert !line.isEmpty() : "Task.toStringFile() returned empty string";
            assert line.contains("|") : "Task string should contain pipe delimiter: " + line;

            fw.write(line + "\n");
        }
        fw.close();
    }
}
