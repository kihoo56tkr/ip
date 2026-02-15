package mintel.model.tasklist;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mintel.exception.DataValidationException;
import mintel.exception.InvalidDateFormatException;
import mintel.exception.MintelException;
import mintel.exception.OutOfRangeException;
import mintel.model.task.Deadline;
import mintel.model.task.Event;
import mintel.model.task.Task;
import mintel.model.task.Todo;

/**
 * Manages a collection of tasks.
 * Provides operations to add, remove, mark, and retrieve tasks.
 */
public class TaskList {
    private final List<Task> tasks;
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
     * Adds a task with duplicate detection.
     *
     * @param task The task to add.
     * @throws DataValidationException If a duplicate task is found.
     */
    public void add(Task task) throws DataValidationException {
        assert task != null : "Cannot add null task";

        if (isDuplicate(task)) {
            throw new DataValidationException("");
        }

        this.tasks.add(task);
        this.taskCount++;

        assert this.tasks.contains(task) : "Added task not found in list";
        assert this.taskCount == this.tasks.size() : "Count should match list size";
        assert this.tasks.get(this.tasks.size() - 1) == task : "Task not at expected position";
    }

    /**
     * Checks if a task is a duplicate of an existing task.
     * Two tasks are considered duplicates if they have:
     * - Same type (Todo/Deadline/Event)
     * - Same description
     * - Same dates (if applicable)
     */
    private boolean isDuplicate(Task newTask) {
        for (Task existingTask : tasks) {
            if (existingTask.getClass() != newTask.getClass()) {
                continue;
            }

            if (newTask instanceof Todo) {
                if (existingTask.getName().equals(newTask.getName())) {
                    return true;
                }
            } else if (newTask instanceof Deadline) {
                Deadline existing = (Deadline) existingTask;
                Deadline newDeadline = (Deadline) newTask;
                if (existing.getName().equals(newDeadline.getName())
                        && existing.getByDate().equals(newDeadline.getByDate())) {
                    return true;
                }
            } else if (newTask instanceof Event) {
                Event existing = (Event) existingTask;
                Event newEvent = (Event) newTask;
                if (existing.getName().equals(newEvent.getName())
                        && existing.getFromDate().equals(newEvent.getFromDate())
                        && existing.getToDate().equals(newEvent.getToDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index The index of the task to remove (0-based).
     * @return The removed task.
     * @throws OutOfRangeException If the index is out of bounds.
     */
    public Task remove(int index) throws MintelException {
        boolean isIndexNegative = index < 0;
        boolean isIndexInTaskList = index >= this.taskCount;
        boolean isIndexValid = isIndexNegative || isIndexInTaskList;

        assert taskCount == tasks.size() : "Count mismatch before removal";

        if (isIndexValid) {
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
        boolean isIndexNegative = index < 0;
        boolean isIndexInTaskList = index >= this.taskCount;
        boolean isIndexValid = isIndexNegative || isIndexInTaskList;

        assert this.taskCount == this.tasks.size() : "Count mismatch before removal";

        if (isIndexValid) {
            throw new OutOfRangeException();
        }
        return this.tasks.get(index);
    }

    /**
     * Marks or unmarks a task at the specified index as done/not done.
     * Retrieves the task from the list and updates its completion status.
     *
     * @param index  The zero-based index of the task to mark/unmark.
     * @param isDone {@code true} to mark the task as done,
     *               {@code false} to mark it as not done.
     * @throws MintelException If the index is out of bounds or the task cannot be accessed.
     * @throws AssertionError  If assertions are enabled and the task is null after retrieval,
     *                         or if the task's completion status does not match the expected value.
     */
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
        int size = this.taskCount;
        assert size >= 0 : "Size cannot be negative: " + size;
        assert size == this.tasks.size() : "Size mismatch: count=" + this.taskCount + ", list=" + this.tasks.size();

        return size;
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        boolean empty = this.tasks.isEmpty();
        boolean countZero = (this.taskCount == 0);

        assert empty == countZero : "Empty state mismatch: list=" + empty + ", count=" + countZero;

        return empty;
    }

    /**
     * Returns all tasks in the list.
     *
     * @return A list containing all tasks.
     */
    public List<Task> getAllTasks() {
        return this.tasks;
    }

    /**
     * Returns a formatted string representation of all tasks.
     * Each task is numbered starting from 1.
     *
     * @return Formatted string of all tasks, or "Your list is empty!" if empty.
     */
    public String getListString() {
        if (this.tasks.isEmpty()) {
            assert this.taskCount == 0 : "Empty list but count > 0";
            return "Meow~ Your list is empty!";
        }

        return Stream.iterate(0, i -> i + 1)
                .limit(tasks.size())
                .map(i -> (i + 1) + "." + tasks.get(i))
                .collect(Collectors.joining("\n",
                        "MEOWRiffic! Here are the tasks in your list:\n", ""));
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

        for (int i = 0; i < this.tasks.size(); i++) {
            Task task = this.tasks.get(i);
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

    /**
     * Returns a formatted schedule of all deadlines and events occurring on a specific date.
     *
     * @param date Target date in yyyy-MM-dd format (e.g., "2026-03-15").
     * @return Formatted schedule string.
     */
    public String getScheduleByDate(String date) throws InvalidDateFormatException {
        assert date != null : "Date cannot be null";
        assert !date.trim().isEmpty() : "Date cannot be empty";

        LocalDate targetDate;
        try {
            targetDate = LocalDate.parse(date);
        } catch (DateTimeParseException e2) {
            throw new InvalidDateFormatException("");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("MEOWRiffic! Schedule for ").append(date).append(":\n");

        int count = 0;

        for (Task task : this.tasks) {
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getByDate().equals(targetDate)) {
                    count++;
                    sb.append(count).append(". ").append(deadline.toString()).append("\n");
                }
            } else if (task instanceof Event) {
                Event event = (Event) task;
                if (isDateInRange(targetDate, event.getFromDate(), event.getToDate())) {
                    count++;
                    sb.append(count).append(". ").append(event.toString()).append("\n");
                }
            }
        }

        if (count == 0) {
            return "Meow~ No deadlines or events scheduled on " + date + ".";
        }

        return sb.toString().trim();
    }

    /**
     * Checks if a target date falls within an event's date range (inclusive).
     *
     * @param target The date to check.
     * @param from   The event start date.
     * @param to     The event end date.
     * @return true if target is between from and to (inclusive).
     */
    private boolean isDateInRange(LocalDate target, LocalDate from, LocalDate to) {
        return (target.isEqual(from) || target.isAfter(from))
                && (target.isEqual(to) || target.isBefore(to));
    }
}
