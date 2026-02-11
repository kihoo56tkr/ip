package mintel.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mintel.model.task.Task;
import mintel.exception.MintelException;

/**
 * Handles loading and saving of tasks to/from a file.
 * Manages file existence checks and format validation.
 */
public class Storage {
    private String filePath;

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

    public boolean fileExists() {
        assert filePath != null : "File path must be initialized";

        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public boolean isFileFormatCorrect() {
        assert filePath != null : "File path must be initialized";

        File taskFile = new File(filePath);
        assert taskFile != null : "File object creation failed";

        try (java.util.Scanner scanner = new java.util.Scanner(taskFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", -1);
                int pipeCount = parts.length - 1;

                assert parts.length > 0 : "Line split resulted in empty array";

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                    assert parts[i] != null : "Trimmed part should not be null";
                }

                if (parts[0].equals("T")) {
                    if (pipeCount != 2 || parts.length != 3) {
                        assert false : "Todo format invalid at line " + line;
                        return false;
                    }
                } else if (parts[0].equals("D")) {
                    if (pipeCount != 3 || parts.length != 4) {
                        assert false : "Deadline format invalid at line " + line;
                        return false;
                    }
                } else if (parts[0].equals("E")) {
                    if (pipeCount != 4 || parts.length != 5) {
                        assert false : "Event format invalid at line " + line;
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        } catch (java.io.FileNotFoundException e) {
            assert false : "File not found despite existence check: " + filePath;
            return true;
        }
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
                if (!line.isEmpty()) {
                    assert !line.contains("\n") : "Line contains newline character";
                    Task task = parseTaskLine(line);
                    if (task != null) {
                        tasks.add(task);
                    }
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

    private Task parseTaskLine(String line) throws MintelException {
        assert line != null : "Line to parse cannot be null";
        assert !line.trim().isEmpty() : "Line to parse cannot be empty";
        assert line.contains("|") : "Line should contain pipe delimiter: " + line;

        return Task.fromFileString(line);
    }
}