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
        assert tasks != null : "ArrayList creation failed";
        assert tasks.isEmpty() : "New TaskList should be empty";

        this.taskCount = 0;
        assert taskCount == 0 : "New TaskList count should be 0";
        assert taskCount == tasks.size() : "Count should match list size";
    }

    /**
     * Constructs a TaskList with existing tasks.
     *
     * @param tasks Initial list of tasks.
     */
    public TaskList(List<Task> tasks) {
        assert tasks != null : "Initial task list cannot be null";

        this.tasks = tasks;
        assert this.tasks != null : "ArrayList copy creation failed";
        assert this.tasks.size() == tasks.size() : "Copy size mismatch";

        this.taskCount = tasks.size();
        assert taskCount >= 0 : "Task count cannot be negative";
        assert taskCount == this.tasks.size() : "Count should match list size";
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        assert task != null : "Cannot add null task";

        tasks.add(task);
        taskCount++;

        assert tasks.contains(task) : "Added task not found in list";
        assert taskCount == tasks.size() : "Count should match list size";
        assert tasks.get(tasks.size() - 1) == task : "Task not at expected position";
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index The index of the task to remove (0-based).
     * @return The removed task.
     * @throws OutOfRangeException If the index is out of bounds.
     */
    public Task remove(int index) throws MintelException {
        assert taskCount == tasks.size() : "Count mismatch before removal";

        if (index < 0 || index >= taskCount) {
            throw new OutOfRangeException();
        }
        Task removedTask = tasks.remove(index);
        taskCount--;

        assert taskCount == tasks.size() : "Count should match list size after removal";

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
        assert taskCount == tasks.size() : "Count mismatch before get";

        if (index < 0 || index >= taskCount) {
            throw new OutOfRangeException();
        }
        return tasks.get(index);
    }

    public void markTask(int index, boolean isDone) throws MintelException {
        Task task = get(index);
        assert task != null : "Task to mark should not be null";

        if (isDone) {
            task.markAsDone();
        } else {
            task.unmarkAsDone();
        }

        assert task.getIsDone() == isDone : "Task marking failed: expected " + isDone + " but got " + task.getIsDone();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks.
     */
    public int size() {
        int size = taskCount;
        assert size >= 0 : "Size cannot be negative: " + size;
        assert size == tasks.size() : "Size mismatch: count=" + taskCount + ", list=" + tasks.size();

        return size;
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        boolean empty = tasks.isEmpty();
        boolean countZero = (taskCount == 0);

        assert empty == countZero : "Empty state mismatch: list=" + empty + ", count=" + countZero;

        return empty;
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
            assert taskCount == 0 : "Empty list but count > 0";
            return "Your list is empty!";
        }

        assert taskCount > 0 : "Non-empty list but count == 0";
        assert taskCount == tasks.size() : "Count mismatch in getListString";

        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        int taskCounter = 0;

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            assert task != null : "Task at index " + i + " is null";

            sb.append((i + 1)).append(".").append(task).append("\n");
            taskCounter++;
        }

        assert taskCounter == tasks.size() : "Not all tasks processed: " + taskCounter + " vs " + tasks.size();

        return sb.toString().trim();
    }

    /**
     * Returns a formatted string representation of all filtered tasks that contains all the keyword(s).
     *
     * @param keywords One or more keywords to filter the tasks.
     * @return Formatted string of all filtered tasks, or empty string if none found.
     */
    public String getFilteredTasks(String... keywords) {
        assert keywords != null : "Keywords array cannot be null";

        if (keywords.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int counter = 1;

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            assert task != null : "Task at index " + i + " is null";

            String taskName = task.getName();
            assert taskName != null : "Task name cannot be null";
            assert !taskName.isEmpty() : "Task name cannot be empty";

            boolean containsAllKeywords = true;
            for (String keyword : keywords) {
                assert keyword != null : "Keyword in loop cannot be null";
                if (!taskName.toLowerCase().contains(keyword.toLowerCase())) {
                    containsAllKeywords = false;
                    break;
                }
            }

            if (containsAllKeywords) {
                sb.append(counter).append(".").append(task).append("\n");
                counter++;
            }
        }

        return sb.toString().trim();
    }
}