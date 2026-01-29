package mintel.model.tasklist;

import java.util.ArrayList;
import java.util.List;

import mintel.model.task.Task;
import mintel.exception.MintelException;
import mintel.exception.OutOfRangeException;
import mintel.exception.EmptyDescriptionException;

public class TaskList {
    private List<Task> tasks;
    private int taskCount;

    public TaskList() {
        this.tasks = new ArrayList<>();
        this.taskCount = 0;
    }

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
        this.taskCount = tasks.size();
    }

    public void add(Task task) {
        tasks.add(task);
        taskCount++;
    }

    public Task remove(int index) throws MintelException {
        if (index < 0 || index >= taskCount) {
            throw new OutOfRangeException();
        }
        Task removedTask = tasks.remove(index);
        taskCount--;
        return removedTask;
    }

    public Task get(int index) throws MintelException {
        if (index < 0 || index >= taskCount) {
            throw new OutOfRangeException();
        }
        return tasks.get(index);
    }

    public void markTask(int index, boolean isDone) throws MintelException {
        Task task = get(index);
        if (isDone) {
            task.markAsDone();
        } else {
            task.unmarkAsDone();
        }
    }

    public int size() {
        return taskCount;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public String getListString() {
        if (tasks.isEmpty()) {
            return "Your list is empty!";
        }

        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1) + "." + tasks.get(i) + "\n");
        }
        return sb.toString();
    }
}