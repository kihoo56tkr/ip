package mintel.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mintel.model.task.Task;
import mintel.exception.MintelException;

public class Storage {
    private String filePath;

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

                if (parts[0].equals("T")) {
                    if (pipeCount != 2 || parts.length != 3) {
                        return false;
                    }
                } else if (parts[0].equals("D")) {
                    if (pipeCount != 3 || parts.length != 4) {
                        return false;
                    }
                } else if (parts[0].equals("E")) {
                    if (pipeCount != 4 || parts.length != 5) {
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

    public ArrayList<Task> loadTasks() throws MintelException {
        ArrayList<Task> tasks = new ArrayList<>();
        File taskFile = new File(filePath);

        if (!taskFile.exists()) {
            return tasks;
        }

        try (java.util.Scanner scanner = new java.util.Scanner(taskFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    Task task = parseTaskLine(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                }
            }
            return tasks;
        } catch (java.io.FileNotFoundException e) {
            throw new MintelException("Error loading tasks: " + e.getMessage());
        }
    }

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