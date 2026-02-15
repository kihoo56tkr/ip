package mintel.model.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import mintel.exception.InvalidDateFormatException;

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
     * @param by          The due date string (yyyy-MM-dd or MMM d yyyy format).
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
     * Uses SMART mode for flexibility but validates against invalid dates.
     *
     * @param dateStr The date string to parse.
     * @return Parsed LocalDate object.
     * @throws InvalidDateFormatException If parsing fails or date is invalid.
     */
    private LocalDate parseDate(String dateStr) throws InvalidDateFormatException {
        // Try MMM d yyyy format first (flexible - allows 1 or 2 digits for day)
        try {
            DateTimeFormatter mmmSmart = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MMM d yyyy")  // Single d for flexible day digits
                    .toFormatter(Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.SMART);

            LocalDate date = LocalDate.parse(dateStr, mmmSmart);

            // Validate that the date wasn't adjusted (e.g., Feb 30 -> Mar 2)
            String roundTrip = date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            if (!roundTrip.equalsIgnoreCase(dateStr) &&
                    !roundTrip.replace("  ", " ").equals(dateStr)) {
                throw new InvalidDateFormatException("Invalid date: " + dateStr +
                        " does not exist. Did you mean " + roundTrip + "?");
            }

            return date;

        } catch (DateTimeParseException e1) {
            // Try yyyy-M-d format (flexible)
            try {
                DateTimeFormatter yyyySmart = DateTimeFormatter.ofPattern("yyyy-M-d")
                        .withResolverStyle(ResolverStyle.SMART);

                LocalDate date = LocalDate.parse(dateStr, yyyySmart);

                // Validate against invalid dates
                String roundTrip = date.toString();
                if (!roundTrip.equals(dateStr)) {
                    // Check if it's just a padding issue (2026-3-5 vs 2026-03-05)
                    String padded = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (!padded.equals(dateStr)) {
                        throw new InvalidDateFormatException("Invalid date: " + dateStr +
                                " does not exist. Did you mean " + roundTrip + "?");
                    }
                }

                return date;

            } catch (DateTimeParseException e2) {
                // Try strict formats as fallback
                return parseDateStrict(dateStr);
            }
        }
    }

    /**
     * Fallback method using strict formats.
     */
    private LocalDate parseDateStrict(String dateStr) throws InvalidDateFormatException {
        DateTimeFormatter mmmStrict = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("MMM dd yyyy")
                .toFormatter(Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT);

        DateTimeFormatter yyyyStrict = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withResolverStyle(ResolverStyle.STRICT);

        try {
            return LocalDate.parse(dateStr, mmmStrict);
        } catch (DateTimeParseException e1) {
            try {
                LocalDate date = LocalDate.parse(dateStr, yyyyStrict);

                // Double-check for invalid dates
                String roundTrip = date.toString();
                if (!roundTrip.equals(dateStr)) {
                    throw new InvalidDateFormatException("Invalid date: " + dateStr +
                            " does not exist. Did you mean " + roundTrip + "?");
                }
                return date;

            } catch (DateTimeParseException e2) {
                throw new InvalidDateFormatException("Invalid date format. " +
                        "Use yyyy-MM-dd or MMM d yyyy (e.g., 2026-03-15 or Mar 15 2026)");
            }
        }
    }

    /**
     * Returns a ByDate of deadline for view schedule.
     *
     * @return LocalDate byDate.
     */
    public LocalDate getByDate() {
        return this.byDate;
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