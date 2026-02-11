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

    private static final int TODO_EXPECTED_PARTS = 3;
    private static final int TODO_EXPECTED_PIPE_COUNT = 2;

    private static final int DEADLINE_EXPECTED_PARTS = 4;
    private static final int DEADLINE_EXPECTED_PIPE_COUNT = 3;

    private static final int EVENT_EXPECTED_PARTS = 5;
    private static final int EVENT_EXPECTED_PIPE_COUNT = 4;

    private static final String TASK_TYPE_TODO = "T";
    private static final String TASK_TYPE_DEADLINE = "D";
    private static final String TASK_TYPE_EVENT = "E";

    /**
     * Constructs a Storage instance with the specified file path.
     *
     * @param filePath The path to the task storage file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public boolean fileExists() {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public boolean isFileFormatCorrect() {
        File taskFile = new File(filePath);

        try (java.util.Scanner scanner = new java.util.Scanner(taskFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", -1);
                int pipeCount = parts.length - 1;

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                if (parts[0].equals(TASK_TYPE_TODO)) {
                    if (pipeCount != TODO_EXPECTED_PIPE_COUNT || parts.length != TODO_EXPECTED_PARTS) {
                        return false;
                    }
                } else if (parts[0].equals(TASK_TYPE_DEADLINE)) {
                    if (pipeCount != DEADLINE_EXPECTED_PIPE_COUNT || parts.length != DEADLINE_EXPECTED_PARTS) {
                        return false;
                    }
                } else if (parts[0].equals(TASK_TYPE_EVENT)) {
                    if (pipeCount != EVENT_EXPECTED_PIPE_COUNT || parts.length != EVENT_EXPECTED_PARTS) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        } catch (java.io.FileNotFoundException e) {
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
        ArrayList<Task> tasks = new ArrayList<>();
        File taskFile = new File(filePath);

        if (!taskFile.exists()) {
            return tasks;
        }

        try (java.util.Scanner scanner = new java.util.Scanner(taskFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                Task task = parseTaskLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            return tasks;
        } catch (java.io.FileNotFoundException e) {
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
        File dataDir = new File("./data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        FileWriter fw = new FileWriter(filePath);
        for (Task task : tasks) {
            fw.write(task.toStringFile() + "\n");
        }
        fw.close();
    }

    private Task parseTaskLine(String line) throws MintelException {
        return Task.fromFileString(line);
    }
}