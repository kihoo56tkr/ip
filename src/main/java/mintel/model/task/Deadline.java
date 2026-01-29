package mintel.model.task;

import mintel.exception.MintelException;
import mintel.exception.InvalidDateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Deadline extends Task {
    protected String displayBy;
    protected LocalDate byDate;

    public Deadline(String description, String by) throws InvalidDateFormatException {
        super(description);
        this.byDate = parseDate(by.trim());
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        this.displayBy = this.byDate.format(displayFormatter);
    }

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

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.displayBy + ")";
    }

    @Override
    public String toStringFile() {
        return "D | " + super.getStatusIconFile() + " | " + super.name + " | " + this.displayBy;
    }
}
