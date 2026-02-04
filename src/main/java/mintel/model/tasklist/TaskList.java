package mintel.model.tasklist;

import java.util.ArrayList;
import java.util.List;

import mintel.model.task.Task;
import mintel.exception.MintelException;
import mintel.exception.OutOfRangeException;

/**
 * Manages a collection of tasks.
 * Provides operations to add, remove, mark, and retrieve tasks.
 */
public class TaskList {
    private List<Task> tasks;
    private int taskCount;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        this.taskCount = 0;
    }

    /**
     * Constructs a TaskList with existing tasks.
     *
     * @param tasks Initial list of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
        this.taskCount = tasks.size();
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        tasks.add(task);
        taskCount++;
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index The index of the task to remove (0-based).
     * @return The removed task.
     * @throws OutOfRangeException If the index is out of bounds.
     */
    public Task remove(int index) throws MintelException {
        if (index < 0 || index >= taskCount) {
            throw new OutOfRangeException();
        }
        Task removedTask = tasks.remove(index);
        taskCount--;
        return removedTask;
    }

    /**
     * Gets the task at the specified index.
     *
     * @param index The index of the task to retrieve (0-based).
     * @return The task at the specified index.
     * @throws OutOfRangeException If the index is out of bounds.
     */
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

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return taskCount;
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns all tasks in the list.
     *
     * @return A list containing all tasks.
     */
    public List<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Returns a formatted string representation of all tasks.
     * Each task is numbered starting from 1.
     *
     * @return Formatted string of all tasks, or "Your list is empty!" if empty.
     */
    public String getListString() {
        if (tasks.isEmpty()) {
            return "Your list is empty!";
        }

        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1) + "." + tasks.get(i) + "\n");
        }
        return sb.toString().trim();
    }

    /**
     * Returns a formatted string representation of all filtered tasks that contains keyword.
     *
     * @param keyword String of the keyword to filter the tasks.
     * @return Formatted string of all filtered tasks, or "" if empty.
     */
    public String getFilteredTasks(String keyword) {
        StringBuilder sb = new StringBuilder("");
        int counter = 1;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getName().contains(keyword)) {
                sb.append(counter + "." + tasks.get(i) + "\n");
                counter += 1;
            }
        }
        return sb.toString().trim();
    }
}