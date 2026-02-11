package mintel.model.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import mintel.exception.DateLogicException;
import mintel.exception.InvalidDateFormatException;

/**
 * Represents a event task that needs to be done before a specific date.
 * Supports date formats: yyyy-MM-dd and MMM d yyyy.
 */
public class Event extends Task {
    protected LocalDate from;
    protected LocalDate to;
    protected String displayFrom;
    protected String displayTo;

    /**
     * Constructs an Event task with description and due date.
     *
     * @param name The description of the event.
     * @param from The starting date string (yyyy-MM-dd or MMM d yyyy format).
     * @param to   The ending date string (yyyy-MM-dd or MMM d yyyy format).
     * @throws InvalidDateFormatException If the date format is invalid.
     */
    public Event(String name, String from, String to) throws InvalidDateFormatException, DateLogicException {
        super(name);
        assert name != null : "Event description cannot be null";
        this.from = parseDate(from.trim());
        this.to = parseDate(to.trim());
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        this.displayFrom = this.from.format(displayFormatter);
        this.displayTo = this.to.format(displayFormatter);
        if (!this.from.isBefore(this.to)) {
            throw new DateLogicException("");
        }
    }

    /**
     * Parses a date string in either "MMM dd yyyy" or "yyyy-MM-dd" format.
     *
     * @param dateStr Date string to parse (e.g., "Mar 15 2026" or "2026-03-15")
     * @return LocalDate object representing the parsed date
     * @throws InvalidDateFormatException if the date format is not supported
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
     * Returns from date of event for view schedule.
     *
     * @return LocalDate from.
     */
    public LocalDate getFromDate() {
        return this.from;
    }

    /**
     * Returns to date of event for view schedule.
     *
     * @return LocalDate to.
     */
    public LocalDate getToDate() {
        return this.to;
    }

    /**
     * Returns a string representation of the event for display.
     * Format: [E][status] description (by: formatted date)
     *
     * @return Formatted string representation.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.displayFrom + " to: " + this.displayTo + ")";
    }

    /**
     * Returns a string representation of the event for file storage.
     * Format: E | status | description | formatted from date | formatted to date
     *
     * @return Formatted string for file storage.
     */
    @Override
    public String toStringFile() {
        return "E | " + super.getStatusIconFile() + " | " + super.name + " | From: "
                + this.displayFrom + " | To: " + this.displayTo;
    }
}
