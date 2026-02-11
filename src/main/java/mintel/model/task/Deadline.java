package mintel.model.task;

import mintel.exception.InvalidDateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Represents a deadline task that needs to be done before a specific date.
 * Supports date formats: yyyy-MM-dd and MMM d yyyy.
 */
public class Deadline extends Task {
    protected String displayBy;
    protected LocalDate byDate;

    /**
     * Constructs a Deadline task with description and due date.
     *
     * @param description The description of the deadline.
     * @param by The due date string (yyyy-MM-dd or MMM d yyyy format).
     * @throws InvalidDateFormatException If the date format is invalid.
     */
    public Deadline(String description, String by) throws InvalidDateFormatException {
        super(description);
        assert description != null : "Deadline description cannot be null";
        this.byDate = parseDate(by.trim());
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        this.displayBy = this.byDate.format(displayFormatter);
    }

    /**
     * Parses a date string into a LocalDate object.
     * Supports: yyyy-MM-dd and MMM d yyyy formats.
     *
     * @param dateStr The date string to parse.
     * @return Parsed LocalDate object.
     * @throws InvalidDateFormatException If parsing fails.
     */
    private LocalDate parseDate(String dateStr) throws InvalidDateFormatException {
        DateTimeFormatter mmmFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("MMM dd yyyy")
                .toFormatter(Locale.ENGLISH);

        DateTimeFormatter yyyyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            return LocalDate.parse(dateStr, mmmFormatter);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(dateStr, yyyyFormatter);
            } catch (DateTimeParseException e2) {
                throw new InvalidDateFormatException("");
            }
        }
    }

    /**
     * Returns a string representation of the deadline for display.
     * Format: [D][status] description (by: formatted date)
     *
     * @return Formatted string representation.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.displayBy + ")";
    }

    /**
     * Returns a string representation of the deadline for file storage.
     * Format: D | status | description | formatted date
     *
     * @return Formatted string for file storage.
     */
    @Override
    public String toStringFile() {
        return "D | " + super.getStatusIconFile() + " | " + super.name + " | " + this.displayBy;
    }
}
