package mintel.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        assert this.filePath.equals(filePath) : "File path not stored correctly";
    }

    /**
     * Checks if the storage file exists.
     *
     * @return true if file exists and is a regular file
     */
    public boolean fileExists() {
        assert filePath != null : "File path must be initialized";

        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    /**
     * Validates the format of the storage file.
     * Checks each line against expected Todo/Deadline/Event formats.
     *
     * @return true if file format is correct or file doesn't exist
     */
    public boolean isFileFormatCorrect() {
        assert filePath != null : "File path must be initialized";
        File taskFile = new File(filePath);
        assert taskFile != null : "File object creation failed";

        try (Scanner scanner = new Scanner(taskFile)) {
            return validateFileContent(scanner);
        } catch (FileNotFoundException e) {
            assert false : "File not found despite existence check: " + filePath;
            return true;
        }
    }

    /**
     * Validates all lines in the file.
     */
    private boolean validateFileContent(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            if (!isLineFormatValid(line)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates a single line from the file.
     */
    private boolean isLineFormatValid(String line) {
        String[] parts = line.split("\\|", -1);
        int pipeCount = parts.length - 1;

        assert parts.length > 0 : "Line split resulted in empty array";

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
            assert parts[i] != null : "Trimmed part should not be null";
        }

        return validateTaskFormat(parts, pipeCount, line);
    }

    /**
     * Checks the task type and validates its format.
     */
    private boolean validateTaskFormat(String[] parts, int pipeCount, String line) {
        if (parts[0].equals(TASK_TYPE_TODO)) {
            return validateTodoFormat(parts, pipeCount, line);
        } else if (parts[0].equals(TASK_TYPE_DEADLINE)) {
            return validateDeadlineFormat(parts, pipeCount, line);
        } else if (parts[0].equals(TASK_TYPE_EVENT)) {
            return validateEventFormat(parts, pipeCount, line);
        } else {
            return false;
        }
    }

    /**
     * Validates a Todo task format.
     */
    private boolean validateTodoFormat(String[] parts, int pipeCount, String line) {
        if (pipeCount != TODO_EXPECTED_PIPE_COUNT || parts.length != TODO_EXPECTED_PARTS) {
            assert false : "Todo format invalid at line " + line;
            return false;
        }
        return true;
    }

    /**
     * Validates a Deadline task format.
     */
    private boolean validateDeadlineFormat(String[] parts, int pipeCount, String line) {
        if (pipeCount != DEADLINE_EXPECTED_PIPE_COUNT || parts.length != DEADLINE_EXPECTED_PARTS) {
            assert false : "Deadline format invalid at line " + line;
            return false;
        }
        return true;
    }

    /**
     * Validates an Event task format.
     */
    private boolean validateEventFormat(String[] parts, int pipeCount, String line) {
        if (pipeCount != EVENT_EXPECTED_PIPE_COUNT || parts.length != EVENT_EXPECTED_PARTS) {
            assert false : "Event format invalid at line " + line;
            return false;
        }
        return true;
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return A list of tasks loaded from the file.
     * @throws MintelException If there's an error reading the file.
     */
    public ArrayList<Task> loadTasks() throws MintelException {
        assert filePath != null : "File path must be initialized";

        ArrayList<Task> tasks = new ArrayList<>();
        assert tasks != null : "Task list creation failed";

        File taskFile = new File(filePath);
        assert taskFile != null : "File object creation failed";

        if (!taskFile.exists()) {
            assert tasks.isEmpty() : "Task list should be empty for non-existent file";
            return tasks;
        }

        assert taskFile.canRead() : "Cannot read task file: " + filePath;
        assert taskFile.length() >= 0 : "File has negative length: " + filePath;

        try (java.util.Scanner scanner = new java.util.Scanner(taskFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                assert !line.contains("\n") : "Line contains newline character";

                Task task = parseTaskLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            return tasks;
        } catch (java.io.FileNotFoundException e) {
            assert false : "File disappeared between existence check and opening: " + filePath;
            throw new MintelException("Error loading tasks: " + e.getMessage());
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

    /**
     * Parses a single line from the storage file into a Task object.
     *
     * @param line Raw line from storage file
     * @return Task object parsed from the line
     * @throws MintelException if line format is invalid
     */
    private Task parseTaskLine(String line) throws MintelException {
        assert line != null : "Line to parse cannot be null";
        assert !line.trim().isEmpty() : "Line to parse cannot be empty";
        assert line.contains("|") : "Line should contain pipe delimiter: " + line;

        return Task.fromFileString(line);
    }
}
