import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Event extends Task {
    protected LocalDate from;
    protected LocalDate to;
    protected String displayFrom;
    protected String displayTo;

    public Event(String name, String from, String to) throws InvalidDateFormatException, DateLogicException {
        super(name);
        this.from = parseDate(from.trim());
        this.to = parseDate(to.trim());
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        this.displayFrom = this.from.format(displayFormatter);
        this.displayTo = this.to.format(displayFormatter);
        if(!this.from.isBefore(this.to)) {
            throw new DateLogicException("");
        }
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
        return "[E]" + super.toString() + " (from: " + this.displayFrom + " to: " + this.displayTo + ")";
    }

    @Override
    public String toStringFile() {
        return "E | " + super.getStatusIconFile() + " | " + super.name + " | From: " + this.displayFrom + " | To: " + this.displayTo;
    }
}

